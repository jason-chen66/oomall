package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.order.dao.OrderDao;
import cn.edu.xmu.oomall.order.dao.OrderItemDao;
import cn.edu.xmu.oomall.order.dao.OrderPaymentDao;
import cn.edu.xmu.oomall.order.dao.OrderRefundDao;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import cn.edu.xmu.oomall.order.dao.bo.OrderPayment;
import cn.edu.xmu.oomall.order.dao.bo.OrderRefund;
import cn.edu.xmu.oomall.order.dao.openfeign.GoodsDao;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.CouponDto;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.OnsaleDto;
import cn.edu.xmu.oomall.order.dao.openfeign.proxy.GoodsDaoProxy;
import cn.edu.xmu.oomall.order.service.orderProcessor.OrderProcessor;
import cn.edu.xmu.oomall.order.service.orderProcessor.OrderProcessorFactory;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class OrderListenerService {
    private static final Logger logger = LoggerFactory.getLogger(OrderListenerService.class);
    private final GoodsDao goodsDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final OrderPaymentDao orderPaymentDao;
    private final OrderRefundDao orderRefundDao;
    private OrderProcessorFactory factory;

    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    public OrderListenerService(@Qualifier("goodsDaoProxy") GoodsDao goodsDao, OrderItemDao orderItemDao, OrderRefundDao orderRefundDao, OrderDao orderDao, OrderPaymentDao orderPaymentDao, OrderProcessorFactory factory, RocketMQTemplate rocketMQTemplate) {
        this.goodsDao = goodsDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.orderPaymentDao = orderPaymentDao;
        this.orderRefundDao = orderRefundDao;
        this.factory = factory;
        this.rocketMQTemplate = rocketMQTemplate;
    }

    /**
     * 团购活动成功订单
     *
     * @param actId 活动id
     * @param user  修改人
     */
    @Transactional
    public void actSuccessOrder(Long actId, UserDto user) throws BusinessException {
        List<OrderItem> orderItems = orderItemDao.findByAct(actId);
        if (orderItems == null) {
            return;
        }
        //获取参与活动明细的orderId，去重
        List<Long> ids = orderItems.stream().map(OrderItem::getOrderId).collect(Collectors.collectingAndThen(
                Collectors.toCollection(HashSet::new), ArrayList::new));

        //根据orderId修改状态
        ids.stream().forEach(id -> {
            Order order = orderDao.findById(id);
            order.setStatus(Order.PAID);
            orderDao.saveById(order, user);
        });

    }

    /**
     * 团购活动失败订单
     *
     * @param actId 活动id
     * @param user  修改人
     */
    @Transactional
    public void actFailOrder(Long actId, UserDto user) throws BusinessException {
        List<OrderItem> orderItems = orderItemDao.findByAct(actId);
        if (orderItems == null) {
            return;
        }
        //获取参与活动明细的orderId，去重
        List<Long> ids = orderItems.stream().map(OrderItem::getOrderId).collect(Collectors.collectingAndThen(
                Collectors.toCollection(HashSet::new), ArrayList::new));

        //根据orderId进行订单全额退款
        ids.stream().forEach(id -> {
            Order order = orderDao.findById(id);
            List<OrderPayment> payments = orderPaymentDao.retrieveOrderPaymentsByOrderId(id);
            for (OrderPayment payment : payments) {
                order.createRefund(payment.getPaymentId());
                String str = JacksonUtil.toJson(order.createRefund(payment.getPaymentId()));
                Message<String> msg = MessageBuilder.withPayload(str).setHeader("order", order.getId()).setHeader("customer", user).build();
                rocketMQTemplate.send("New-refund:1", msg);
            }
            order.setStatus(Order.NOT_REFUNDED);
            orderDao.saveById(order, user);
            //向customer模块发出消息，恢复积点
            if (order.getPoint() != null) {
                sendMsgWithHead(order.getPoint(), "Resume-Points:1", "customer", order.getCustomerId());
            }

            //向customer模块发出消息，返还优惠券
            List<Long> coupons = order.getOrderItems().stream().map(OrderItem::getCouponId).collect(Collectors.toList());
            if (!(coupons.size() == 0)) {
                sendMsgWithHead(order.getPoint(), "Resume-Coupons:1", "customer", order.getCustomerId());
            }
        });
    }

    /**
     * 修改订单，写支付单，若point不为空,按比例修改订单，减少顾客积点
     *
     * @param bo
     * @param user
     * @throws BusinessException
     */
    @Transactional
    public void saveOrderPayment(Order order, OrderPayment bo, UserDto user) throws BusinessException {
        Long onSaleId = order.getOrderItems().get(0).getOnsaleId();
        OnsaleDto onsaleDto = goodsDao.getOnsaleById(order.getShopId(), onSaleId).getData();
        String beanName = OnsaleDto.BEANNAMES.get(onsaleDto.getType());
        OrderProcessor orderProcessor = this.factory.createOrderProcessor(beanName);

        try {
            //回写支付单
            orderPaymentDao.save(bo, user);

            //使用Processor改写订单状态
            orderProcessor.orderPayStatus(order);
            orderDao.saveById(order, user);
            Long point = order.getPoint();
            List<Long> coupons = order.getOrderItems().stream().map(OrderItem::getCouponId).collect(Collectors.toList());
            //若使用积点或者优惠券，则写入order和明细.发送消息到顾客模块减少积点/减少优惠券
            if (point != null || coupons.size() != 0) {
                order.getOrderItems().forEach(orderItem -> {
                    orderItemDao.saveById(orderItem, user);
                });
                //向customer模块发出消息，恢复积点
                if (order.getPoint() != null) {
                    sendMsgWithHead(order.getPoint(), "Reduce-Points:1", "customer", order.getCustomerId());
                }

                //向customer模块发出消息，返还优惠券
                if (!(coupons.size() == 0)) {
                    sendMsgWithHead(order.getPoint(), "Reduce-Coupons:1", "customer", order.getCustomerId());
                }
            }
        } catch (RuntimeException e) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
        }
    }

    /**
     * 修改订单，写退款单，根据状态恢复库存，恢复积点,退还优惠券
     *
     * @param order
     * @param user
     * @throws BusinessException
     */
    @Transactional
    public void saveOrderRefund(Order order, OrderRefund orderRefund, UserDto user) throws BusinessException {
        order.setStatus(Order.REFUNDED);
        orderDao.saveById(order, user);
        if (orderRefund != null)
            orderRefundDao.save(orderRefund, user);

        //向goods模块发出消息，恢复库存，需要的参数是order和orderItems
        if (order.getStatus() <= Order.NOT_SENT) {
            order.findOrderItems();
            sendMsg("order", "Revoke-Order:1");
        }
        //向customer模块发出消息，恢复积点
        if (order.getPoint() != null) {
            sendMsgWithHead(order.getPoint(), "Resume-Points:1", "customer", order.getCustomerId());
        }

        //向customer模块发出消息，返还优惠券
        List<Long> coupons = order.getOrderItems().stream().map(OrderItem::getCouponId).collect(Collectors.toList());
        if (!(coupons.size() == 0)) {
            sendMsgWithHead(order.getPoint(), "Resume-Coupons:1", "customer", order.getCustomerId());
        }
    }

    /**
     * 发送消息操作 RocketMQ
     *
     * @param payLoad
     * @param destination
     */
    public void sendMsg(Object payLoad, String destination) {
        String str = JacksonUtil.toJson(payLoad);
        Message<String> msg = MessageBuilder.withPayload(str).build();
        rocketMQTemplate.send(destination, msg);
    }

    /**
     * 发送消息操作（1个头部信息） RocketMQ
     *
     * @param payLoad
     * @param destination
     */
    public void sendMsgWithHead(Object payLoad, String destination, String headerName, Object head) {
        String str = JacksonUtil.toJson(payLoad);
        Message<String> msg = MessageBuilder.withPayload(str).setHeader(headerName, head).build();
        rocketMQTemplate.send(destination, msg);
    }

}

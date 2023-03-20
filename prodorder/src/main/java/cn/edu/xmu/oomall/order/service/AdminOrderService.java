package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.dao.OrderDao;
import cn.edu.xmu.oomall.order.dao.OrderItemDao;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.openfeign.GoodsDao;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.OnsaleDto;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.PackageDto;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.RefundCheckDto;
import cn.edu.xmu.oomall.order.service.dto.OrderInfoDto;
import cn.edu.xmu.oomall.order.service.dto.SimpleOrderInfoDto;
import cn.edu.xmu.oomall.order.service.orderProcessor.OrderProcessor;
import cn.edu.xmu.oomall.order.service.orderProcessor.OrderProcessorFactory;
import com.github.pagehelper.PageInfo;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@Repository
public class AdminOrderService {
    @Value("${oomall.order.server-num}")
    private int serverNum;

    private static final Logger logger = LoggerFactory.getLogger(AdminOrderService.class);

    private GoodsDao goodsDao;

    private OrderDao orderDao;

    private RocketMQTemplate rocketMQTemplate;

    private OrderItemDao orderItemDao;

    private OrderProcessorFactory factory;

    @Autowired
    public AdminOrderService(@Qualifier("goodsDaoProxy") GoodsDao goodsDao, OrderDao orderDao, OrderItemDao orderItemDao, RocketMQTemplate rocketMQTemplate, OrderProcessorFactory factory) {
        this.goodsDao = goodsDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.rocketMQTemplate = rocketMQTemplate;
        this.factory = factory;
    }

    /**
     * 店家查询商户所有订单（概要）
     *
     * @param shopId     店铺id
     * @param customerId 用户id
     * @param orderSn    订单编号
     * @param beginTime  订单创建时间起始
     * @param endTime    订单创建时间结束
     * @param page       页码
     * @param pageSize   页大小
     * @return 订单概要列表
     */
    public PageDto<SimpleOrderInfoDto> retrieveOrders(Long shopId, Long customerId, String orderSn, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize) {
        PageInfo<Order> list = orderDao.retrieveOrdersByShopIdAndCustomerIdAndOrderSnAndBetweenTime(shopId, customerId, orderSn, beginTime, endTime, page, pageSize);
        return new PageDto<SimpleOrderInfoDto>(list.getList().stream().map(item -> SimpleOrderInfoDto.builder().id(item.getId()).status(item.getStatus()).gmtCreate(item.getGmtCreate()).originPrice(item.getOriginPrice()).discountPrice(item.getDiscountPrice()).freightPrice(item.getExpressFee()).build()).collect(Collectors.toList()), list.getPageNum(), list.getPageSize());
    }

    /**
     * 店家查询指定订单详情
     *
     * @param shopId 店铺id
     * @param id     订单id
     * @return 订单详情
     */
    @Transactional
    public OrderInfoDto findOrder(Long shopId, Long id, UserDto admin) throws BusinessException {
        //店铺名下检查
        Order order = validateOrderGrant(id, shopId);
        //发起运费/支付模块的查询，更新订单
        orderCheck(order, admin);
        // 获取返回最新的订单信息
        return order.gotOrderInfo();
    }

    public void orderCheck(Order order, UserDto user) throws BusinessException {
        //查询支付
        if (order.getStatus().equals(Order.NOT_REFUNDED)) {
            List<RefundCheckDto> rets = order.checkOrderRefunds();
            boolean flag = true;
            for (RefundCheckDto ret : rets) {
                if (!ret.getStatus().equals(RefundCheckDto.SUCCESS))
                    flag = false;
            }
            if (flag) {
                order.setStatus(Order.REFUNDED);
                orderDao.saveById(order, user);
            }
        }
        //查询运单
        else {
            if (order.getPack().getStatus() == PackageDto.ON && order.allowStatus(Order.SENT)) {
                order.setStatus(Order.SENT);
                orderDao.saveById(order, user);
            } else if (order.getPack().needRefund() && order.allowStatus(Order.NOT_REFUNDED)) {
                order.setStatus(Order.NOT_REFUNDED);
                orderDao.saveById(order, user);
            }
        }
    }

    /**
     * 店铺修改备注
     *
     * @param shopId  店铺id
     * @param id      订单id
     * @param message 备注
     * @param admin   管理员信息
     */
    @Transactional
    public void updateOrder(Long shopId, Long id, String message, UserDto admin) throws BusinessException {
        //店铺名下检查
        Order order = validateOrderGrant(id, shopId);
        //检查状态订单未结束
        if (!order.getStatus().equals(Order.CANCELED) && !order.getStatus().equals(Order.SUCCESS)) {
            //检查修改
            if (!Objects.equals(message, order.getMessage())) {
                try {
                    order.setMessage(message);
                    orderDao.saveById(order, admin);
                } catch (RuntimeException e) {
                    throw new BusinessException(ReturnNo.STATENOTALLOW);
                }
            } else {
                throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage()));
            }
        }
    }

    /**
     * 店铺取消订单 RocketMQ Template
     *
     * @param shopId 店铺id
     * @param id     订单id
     * @param admin  管理员信息
     */
    @Transactional
    public void cancelOrder(Long shopId, Long id, UserDto admin) {
        //店铺名下检查
        Order order = validateOrderGrant(id, shopId);

        //根据订单类型，选择是否发送消息&发送几条消息到payment模块创建退款
        Long onSaleId = order.findOrderItems().get(0).getOnsaleId();
        OnsaleDto onsaleDto = goodsDao.getOnsaleById(order.getShop().getId(), onSaleId).getData();
        String beanName = OnsaleDto.BEANNAMES.get(onsaleDto.getType());
        OrderProcessor orderProcessor = this.factory.createOrderProcessor(beanName);
        orderProcessor.orderRefundStatus(order, OrderProcessor.CUSTOMER_CANCEL, admin);
        //回写当前order被改变后的状态
        orderDao.saveById(order, admin);
    }

    /**
     * 店铺确认订单
     *
     * @param shopId 店铺id
     * @param id     订单id
     * @param admin  管理员信息
     */
    @Transactional
    public void confirmOrder(Long shopId, Long id, UserDto admin) throws BusinessException {
        //店铺名下检查
        Order order = validateOrderGrant(id, shopId);

        //状态为待发货
        if (order.getStatus().equals(Order.NOT_SENT)) {
            //创建运单，更新bo对象packageId和状态
            order.createPackage();
            //保存包裹id
            orderDao.saveById(order, admin);
        } else {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "订单", order.getId(), order.getStatusName()));
        }
    }

    /**
     * 检查订单是否在店铺名下
     *
     * @param id
     * @param shopId
     * @return
     * @throws BusinessException
     */
    public Order validateOrderGrant(Long id, Long shopId) throws BusinessException {
        Order order;
        try {
            order = orderDao.findById(id);
        } catch (RuntimeException e) {
            if (e.getMessage().equals(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage())) {
                throw e;
            } else {
                throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
            }
        }
        if (!shopId.equals(PLATFORM) && !order.getShop().getId().equals(shopId)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "订单", id, shopId));
        }
        return order;
    }
}

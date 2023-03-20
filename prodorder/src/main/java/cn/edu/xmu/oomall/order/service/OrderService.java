//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.Common;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.order.controller.vo.OrderItemVo;
import cn.edu.xmu.oomall.order.dao.OrderDao;
import cn.edu.xmu.oomall.order.dao.OrderItemDao;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import cn.edu.xmu.oomall.order.dao.openfeign.CustomerDao;
import cn.edu.xmu.oomall.order.dao.openfeign.GoodsDao;
import cn.edu.xmu.oomall.order.dao.openfeign.ShopDao;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.*;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.proxy.OnSaleQuantityProxy;
import cn.edu.xmu.oomall.order.dao.openfeign.vo.DiscountVo;
import cn.edu.xmu.oomall.order.dao.openfeign.vo.FreightVo;
import cn.edu.xmu.oomall.order.dao.openfeign.vo.PaymentVo;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderItemPo;
import cn.edu.xmu.oomall.order.service.dto.*;
import cn.edu.xmu.oomall.order.service.orderProcessor.OrderProcessor;
import cn.edu.xmu.oomall.order.service.orderProcessor.OrderProcessorFactory;
import com.github.pagehelper.PageInfo;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Repository
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Value("${oomall.order.server-num}")
    private int serverNum;

    private final GoodsDao goodsDao;

    private final OrderDao orderDao;

    private final OrderItemDao orderItemDao;

    private final ShopDao shopDao;

    private final RedisUtil redisUtil;

    private final RocketMQTemplate rocketMQTemplate;

    private final OrderProcessorFactory factory;


    @Autowired
    public OrderService(@Qualifier("goodsDaoProxy") GoodsDao goodsDao, @Qualifier("shopDaoProxy") ShopDao shopDao, OrderDao orderDao, OrderItemDao orderItemDao, RocketMQTemplate rocketMQTemplate, RedisUtil redisUtil, OrderProcessorFactory factory) {
        this.goodsDao = goodsDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.shopDao = shopDao;
        this.rocketMQTemplate = rocketMQTemplate;
        this.redisUtil = redisUtil;
        this.factory = factory;
    }

    /**
     * 检查在售商品库存
     *
     * @param item      订单项
     * @param onsaleDto 在售商品信息
     * @throws BusinessException <p>onsle id 对应的在售商品不存在</p>
     *                           <p>超过最大可购买量</p>
     *                           <p>库存不足</p>
     */
    private void checkQuantity(OrderItemVo item, OnsaleDto onsaleDto) {
        // 不能超过最大可购买量
        if (item.getQuantity() > onsaleDto.getMaxQuantity()) {
            throw new BusinessException(ReturnNo.ITEM_OVERMAXQUANTITY, String.format(ReturnNo.ITEM_OVERMAXQUANTITY.getMessage(), onsaleDto.getId(), item.getQuantity(), onsaleDto.getMaxQuantity()));
        }
        // 不能超过库存
        if (item.getQuantity() > onsaleDto.getQuantity()) {
            throw new BusinessException(ReturnNo.GOODS_STOCK_SHORTAGE, String.format(ReturnNo.GOODS_STOCK_SHORTAGE.getMessage(), onsaleDto.getId()));
        }
    }

    /**
     * 计算订单项的优惠价格
     *
     * @param orderItem 订单项
     * @param onsaleDto 订单项对应的在售商品
     * @param actId     活动 id
     */
    private void calculateAndSetDiscountPrice(OrderItem orderItem, OnsaleDto onsaleDto, Long actId) {
        // 如果 actId 不为空，但是商品没有参与任何优惠活动
        if (null != actId && (null == onsaleDto.getActList() || onsaleDto.getActList().isEmpty())) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "活动", actId));
        }
        if (null == actId || !OnsaleDto.NORMAL.equals(onsaleDto.getType())) {
            // actId 为空，或者商品是团购或预售商品，跳过计算优惠
            orderItem.setDiscountPrice(onsaleDto.getPrice() * orderItem.getQuantity());
        } else if (onsaleDto.getActList().stream().map(IdNameTypeDto::getId).noneMatch(id -> id.equals(actId))) {
            // actId 不为空，但是不在商品所支持的优惠活动中
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "活动", actId));
        } else {
            // 计算优惠
            CouponActivityDto couponActivityDto = goodsDao.getCouponActivityById(onsaleDto.getShop().getId(), actId).getData();
            if (null != couponActivityDto && 0 == couponActivityDto.getQuantity()) {
                List<DiscountVo> discountVoList = List.of(DiscountVo.builder().id(orderItem.getOrderId()).quantity(orderItem.getQuantity()).build());
                List<DiscountDto> discountDtoList = goodsDao.calculateDiscount(actId, discountVoList).getData();
                orderItem.setDiscountPrice(discountDtoList.get(0).getDiscount() * discountDtoList.get(0).getQuantity());
                orderItem.setActId(actId);
            }
        }
    }

    /**
     * 按照 shopId 分组订单项
     *
     * @param items    订单 vo
     * @param customer 订单创建者
     * @return 按照 shopId 分组 map
     */
    public Map<Long, List<OrderItem>> packOrderByShopId(List<OrderItemVo> items, UserDto customer) {
        Map<Long, List<OrderItem>> packs = new HashMap<>(items.size());
        items.forEach(item -> {
            OnSaleQuantityProxy onsaleDto = new OnSaleQuantityProxy(item.getOnsaleId(), redisUtil, goodsDao);
            checkQuantity(item, onsaleDto);
            onsaleDto.descQuantity(item.getQuantity());

            OrderItem orderItem = onsaleDto.parseToBo();
            orderItem.setCreatorId(customer.getId());
            orderItem.setCreatorName(customer.getName());
            orderItem.setGmtCreate(LocalDateTime.now());

            calculateAndSetDiscountPrice(orderItem, onsaleDto, item.getActId());

            Long shopId = onsaleDto.getShop().getId();
            List<OrderItem> pack = packs.get(shopId);
            if (null == pack) {
                packs.put(shopId, new ArrayList<>());
                pack = packs.get(shopId);
            }
            pack.add(orderItem);
        });
        return packs;
    }

    /**
     * 判断订单编号是否存在对应的订单
     *
     * @param orderSn 订单编号
     * @return <p>true 存在对应订单</p>
     * <p>false 不存在对应订单</p>
     */
    public Boolean checkIfExistByOrderSn(String orderSn) {
        return null != orderDao.findByOrderSn(orderSn);
    }

    /**
     * 保存一批订单
     *
     * @param orderList 订单列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrders(List<Order> orderList) {
        orderList.forEach((order) -> {
            orderDao.createOrder(order);
            Map<Long, List<OrderItem>> packs = saveOrderItemsAnPackByTemplateId(order.getOrderItems(), order.getShopId());
            saveSubOrders(packs, order);
            // 更新父订单
            orderDao.saveById(order, null);
            // 减去货品数量
            String orderStr = JacksonUtil.toJson(order);
            Message<String> msg = MessageBuilder.withPayload(orderStr).build();
            rocketMQTemplate.asyncSend("New-Order", msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    logger.info("sendNewOrderMessage: onSuccess result = " + sendResult + " time =" + LocalDateTime.now());
                }

                @Override
                public void onException(Throwable throwable) {
                    logger.info("sendNewOrderMessage: onException e = " + throwable.getMessage() + " time =" + LocalDateTime.now());
                }
            });
        });
    }

    /**
     * 根据运费分包结果创建子订单
     *
     * @param packs       运费分包
     * @param parentOrder 父订单
     */
    private void saveSubOrders(Map<Long, List<OrderItem>> packs, Order parentOrder) {
        packs.keySet().forEach(templateId -> {
            // 获取运费
            FreightDto freightDto = shopDao.getFreightPrice(templateId, parentOrder.getConsignee().getRegionId(), packs.get(templateId).stream().map(item -> {
                ProductDto productDto = goodsDao.getProductByIdAndShopId(parentOrder.getShopId(), item.getProductId()).getData();
                return FreightVo.builder().orderItemId(item.getId()).productId(0L).quantity(item.getQuantity()).weight(productDto.getWeight()).build();
            }).collect(Collectors.toList())).getData();
            // 创建子订单
            freightDto.getPack().forEach(subOrderPack -> {
                Order subOrder = Order.builder().pid(parentOrder.getId()).status(Order.NEW).expressFee(subOrderPack.getPrice()).customerId(parentOrder.getCustomerId()).shopId(parentOrder.getShopId()).orderSn(parentOrder.getOrderSn()).consignee(parentOrder.getConsignee()).message(parentOrder.getMessage()).creatorId(parentOrder.getCreatorId()).creatorName(parentOrder.getCreatorName()).gmtCreate(parentOrder.getGmtCreate()).build();
                subOrderPack.getPack().forEach(subOrderItem -> {
                    // 更新订单项 orderId
                    orderItemDao.saveById(OrderItem.builder().id(subOrderItem.getOrderItemId()).orderId(subOrder.getId()).build(), null);
                    // 设置子订单原总价和折扣总价
                    packs.get(templateId).stream().filter((item) -> item.getId().equals(subOrderItem.getOrderItemId())).findFirst().ifPresent(item -> {
                        subOrder.setOriginPrice(subOrder.getOriginPrice() + item.getPrice());
                        subOrder.setDiscountPrice(subOrder.getDiscountPrice() + item.getDiscountPrice());
                    });
                });
                orderDao.createOrder(subOrder);
                // 设置父订单原总价、折扣总价和运费
                parentOrder.setOriginPrice(parentOrder.getOriginPrice() + subOrder.getOriginPrice());
                parentOrder.setDiscountPrice(parentOrder.getDiscountPrice() + subOrder.getDiscountPrice());
                parentOrder.setExpressFee(parentOrder.getExpressFee() + freightDto.getFreightPrice());
            });
        });
    }

    /**
     * 按照运费模板分组订单
     */
    private Map<Long, List<OrderItem>> saveOrderItemsAnPackByTemplateId(List<OrderItem> orderItems, Long shopId) {
        Map<Long, List<OrderItem>> packs = new HashMap<>(orderItems.size());
        orderItems.forEach(orderItem -> {
            OrderItemPo orderItemPo = OrderItemPo.builder().onsaleId(orderItem.getOnsaleId()).quantity(orderItem.getQuantity()).price(orderItem.getPrice()).discountPrice(orderItem.getDiscountPrice()).name(orderItem.getName()).activityId(orderItem.getActId()).creatorId(orderItem.getCreatorId()).creatorName(orderItem.getCreatorName()).gmtCreate(orderItem.getGmtCreate()).build();
            orderItemDao.insert(orderItemPo);
            // 按照运费模板分单
            ProductDto productDto = goodsDao.getProductByIdAndShopId(shopId, orderItem.getProductId()).getData();
            TemplateDto templateDto = productDto.getTemplateDto();
            List<OrderItem> pack = packs.get(templateDto.getId());
            if (null == pack) {
                packs.put(templateDto.getId(), new ArrayList<>());
                pack = packs.get(templateDto.getId());
            }
            pack.add(orderItem);
        });
        return packs;
    }

    /**
     * 创建订单
     *
     * @param items     订单项
     * @param consignee 收货信息
     * @param message   留言
     * @param customer  用户
     */
    @Transactional
    public void createOrder(List<OrderItemVo> items, ConsigneeDto consignee, String message, UserDto customer) {
        Map<Long, List<OrderItem>> shopIdOrderMap = packOrderByShopId(items, customer);
        Message<String> msg = createOrderBoAndConvertToMessage(shopIdOrderMap, consignee, message, customer);
        TransactionSendResult result = rocketMQTemplate.sendMessageInTransaction("order-topic:1", msg, null);
        if (LocalTransactionState.COMMIT_MESSAGE != result.getLocalTransactionState()) {
            logger.info("create oder failure: {}", msg);
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, ReturnNo.INTERNAL_SERVER_ERR.getMessage());
        }
    }

    private Message<String> createOrderBoAndConvertToMessage(Map<Long, List<OrderItem>> shopIdOrderMap, ConsigneeDto consignee, String message, UserDto customer) {
        List<Order> orderList = shopIdOrderMap.entrySet()
                .stream()
                .map((entry) -> Order.builder()
                        // 生成订单编号
                        .orderSn(Common.genSeqNum(serverNum))
                        // 收件人信息
                        .consignee(consignee)
                        // 订单留言
                        .message(message)
                        // 店铺
                        .shopId(entry.getKey())
                        // 买家信息
                        .customerId(customer.getId())
                        // 订单项
                        .orderItems(entry.getValue())
                        .creatorId(customer.getId()).creatorName(customer.getName()).gmtCreate(LocalDateTime.now())
                        .build()
                ).collect(Collectors.toList());
        String orderListStr = JacksonUtil.toJson(orderList);
        if (null == orderListStr) {
            logger.info("orderListStr is null");
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, ReturnNo.INTERNAL_SERVER_ERR.getMessage());
        }
        Message<String> msg = MessageBuilder.withPayload(orderListStr).build();
        return msg;
    }

    /**
     * 顾客支付订单 RocketMQ Template
     *
     * @param id       订单id
     * @param point    积点
     * @param customer 顾客
     */
    public void payOrder(Long id, Long point, Long shopChannel, List<Long> coupons, UserDto customer) {
        //顾客名下检查
        Order order = validateOrderGrant(id, customer);
        //调用支付模块，发消息回写支付单，订单，订单明细
        PaymentVo vo = order.createPayment(shopChannel, point, coupons);
        String payStr = JacksonUtil.toJson(vo);
        Message<String> payMsg = MessageBuilder.withPayload(payStr).setHeader("order", order).setHeader("user", customer).build();
        rocketMQTemplate.send("New-Payment:1", payMsg);

    }

    /**
     * 获取订单所有状态
     *
     * @return 订单状态列表
     */
    public List<StatusDto> retrieveOrderStates() {
        return Order.STATUSNAMES.keySet().stream().map(key -> new StatusDto(key, Order.STATUSNAMES.get(key))).collect(Collectors.toList());
    }

    /**
     * 顾客查询指定订单
     *
     * @param id       订单id
     * @param customer 顾客
     * @return 订单详情
     */
    @Transactional
    public OrderInfoDto findOrder(Long id, UserDto customer) throws BusinessException {
        //顾客名下检查
        Order order = validateOrderGrant(id, customer);
        //根据当前状态发起退款||运单的最新状态的查询，用于更新订单状态
        orderCheck(order, customer);
        // 获取返回最新的订单信息
        return order.gotOrderInfo();
    }

    /**
     * 根据订单状态进行外部查询
     *
     * @param order
     * @param user
     * @throws BusinessException
     */
    public void orderCheck(Order order, UserDto user) throws BusinessException {
        //查询退款
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
     * 顾客发货前修改订单
     *
     * @param id       订单id
     * @param regionId 地区id
     * @param address  详细地址
     * @param customer 顾客
     */
    @Transactional
    public void updateOrder(Long id, String name, String mobile, Long regionId, String address, UserDto customer) throws BusinessException {
        //顾客名下检查
        Order order = validateOrderGrant(id, customer);

        //检查状态未发货
        if (order.allowStatus(Order.NOT_SENT)) {
            ConsigneeDto consignee = order.getConsignee();
            //检查，若相同则不修改
            if (!address.equals(consignee.getAddress()) || !regionId.equals(consignee.getRegionId()) || !name.equals(order.getConsignee().getName()) || !mobile.equals(consignee.getMobile())) {
                order.getConsignee().setRegionId(regionId);
                order.getConsignee().setAddress(address);
                order.getConsignee().setName(name);
                order.getConsignee().setMobile(mobile);
                orderDao.saveById(order, customer);
            }
        } else {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage()));
        }
    }


    /**
     * 顾客取消订单 RocketMQ Template
     *
     * @param id       订单id
     * @param customer 顾客
     */
    @Transactional
    public void cancelOrder(Long id, UserDto customer) throws BusinessException {
        //顾客名下检查
        Order order = validateOrderGrant(id, customer);

        //根据订单类型，选择是否发送消息&发送几条消息到payment模块创建退款
        Long onSaleId = order.findOrderItems().get(0).getOnsaleId();
        OnsaleDto onsaleDto = goodsDao.getOnsaleById(order.getShopId(), onSaleId).getData();
        String beanName = OnsaleDto.BEANNAMES.get(onsaleDto.getType());
        OrderProcessor orderProcessor = this.factory.createOrderProcessor(beanName);
        orderProcessor.orderRefundStatus(order, OrderProcessor.CUSTOMER_CANCEL, customer);
        //回写当前order被改变后的状态
        orderDao.saveById(order, customer);
    }


    /**
     * 顾客查询名下所有的订单
     *
     * @param orderSn   订单编号
     * @param status    订单状态
     * @param beginTime 订单创建时间起始
     * @param endTime   订单创建时间终止
     * @param page      分页页数
     * @param pageSize  分页大小
     * @param user      顾客
     * @return 订单列表
     */
    public PageDto<SimpleOrderInfoDto> retrieveOrders(String orderSn, Integer status, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize, UserDto user) {
        PageInfo<Order> list = orderDao.retrieveOrdersByCustomerIdAndOrderSnAndBetweenSuccessTime(user.getId(), orderSn, status, beginTime, endTime, page, pageSize);
        return new PageDto<SimpleOrderInfoDto>(list.getList().stream().map(item -> SimpleOrderInfoDto.builder().id(item.getId()).status(item.getStatus()).gmtCreate(item.getGmtCreate()).originPrice(item.getOriginPrice()).discountPrice(item.getDiscountPrice()).freightPrice(item.getExpressFee()).build()).collect(Collectors.toList()), list.getPageNum(), list.getPageSize());
    }

    /**
     * 检查订单是否在店铺名下
     *
     * @param id
     * @param customer
     * @return
     * @throws BusinessException
     */
    public Order validateOrderGrant(Long id, UserDto customer) throws BusinessException {
        Order order;
        // 查找顾客订单，检查是否存在
        try {
            order = orderDao.findById(id);
            logger.info("find order by id: {}", order);
        } catch (BusinessException e) {
            if (e.getMessage().equals(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()) || e.getMessage().equals(ReturnNo.PARAMETER_MISSED.getMessage())) {
                throw e;
            } else {
                throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
            }
        }
        // 检查订单是否在顾客名下
        if (!order.getCustomerId().equals(customer.getId())) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "订单", id));
        }
        return order;
    }
}

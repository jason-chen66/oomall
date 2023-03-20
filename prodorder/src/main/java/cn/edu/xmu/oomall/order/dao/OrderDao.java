//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.openfeign.*;
import cn.edu.xmu.oomall.order.mapper.generator.OrderPoMapper;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderPo;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderPoExample;
import cn.edu.xmu.oomall.order.service.dto.ConsigneeDto;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.putGmtFields;
import static cn.edu.xmu.javaee.core.util.Common.putUserFields;

@Repository
public class OrderDao {

    private static final Logger logger = LoggerFactory.getLogger(OrderDao.class);

    private final OrderPoMapper orderPoMapper;

    private final OrderItemDao orderItemDao;

    private final OrderPaymentDao orderPaymentDao;

    private final OrderRefundDao orderRefundDao;

    public static final String KEY = "O%d";

    @Value("${oomall.order.order.timeout}")
    private long timeout;

    private final RedisUtil redisUtil;

    private PaymentDao paymentDao;

    private GoodsDao goodsDao;

    private CustomerDao customerDao;

    private ShopDao shopDao;

    private FreightDao freightDao;

    @Autowired
    @Lazy
    public OrderDao(OrderPoMapper orderPoMapper,
                    OrderItemDao orderItemDao,
                    OrderPaymentDao orderPaymentDao,
                    OrderRefundDao orderRefundDao,
                    PaymentDao paymentDao,
                    @Qualifier("goodsDaoProxy") GoodsDao goodsDao,
                    @Qualifier("customerDaoProxy") CustomerDao customerDao,
                    @Qualifier("shopDaoProxy") ShopDao shopDao,
                    FreightDao freightDao,
                    RedisUtil redisUtil) {

        this.orderPoMapper = orderPoMapper;
        this.orderItemDao = orderItemDao;
        this.orderPaymentDao = orderPaymentDao;
        this.orderRefundDao = orderRefundDao;
        this.paymentDao = paymentDao;
        this.goodsDao = goodsDao;
        this.customerDao = customerDao;
        this.shopDao = shopDao;
        this.freightDao = freightDao;
        this.redisUtil = redisUtil;
    }

    /**
     * 通过 po 获取 bo
     *
     * @param po       po
     * @param redisKey redis 键
     * @return bo
     */
    private Order getBo(OrderPo po, Optional<String> redisKey) {
        Order bo = Order.builder()
                .id(po.getId())
                .orderSn(po.getOrderSn())
                .packageId(po.getPackageId())
                .consignee(ConsigneeDto.builder().name(po.getConsignee()).regionId(po.getRegionId()).address(po.getAddress()).mobile(po.getMobile()).build())
                .message(po.getMessage())
                .expressFee(po.getExpressFee())
                .discountPrice(po.getDiscountPrice())
                .originPrice(po.getOriginPrice())
                .point(po.getPoint())
                .status(po.getStatus())
                .pid(po.getPid())
                .customerId(po.getCustomerId())
                .shopId(po.getShopId())
                .gmtCreate(po.getGmtCreate()).gmtModified(po.getGmtModified()).creatorId(po.getCreatorId()).modifierId(po.getModifierId()).creatorName(po.getCreatorName()).modifierName(po.getModifierName())
                .build();
        setBo(bo);
        redisKey.ifPresent(s -> redisUtil.set(s, bo, timeout));
        return bo;
    }

    /**
     * 设置 bo 中的 dao
     *
     * @param bo bo对象
     */
    private void setBo(Order bo) {
        bo.setOrderItemDao(this.orderItemDao);
        bo.setOrderPaymentDao(this.orderPaymentDao);
        bo.setOrderRefundDao(this.orderRefundDao);
        bo.setOrderDao(this);
        bo.setPaymentDao(this.paymentDao);
        bo.setShopDao(this.shopDao);
        bo.setCustomerDao(this.customerDao);
        bo.setFreightDao(this.freightDao);
        bo.setGoodsDao(goodsDao);
    }

    /**
     * 创建订单
     *
     * @param order 订单
     */
    public void createOrder(Order order) throws RuntimeException {
        OrderPo orderPo = OrderPo.builder()
                .customerId(order.getCustomerId())
                .shopId(order.getShopId())
                .orderSn(order.getOrderSn())
                .consignee(order.getConsignee().getName()).regionId(order.getConsignee().getRegionId()).address(order.getConsignee().getAddress()).mobile(order.getConsignee().getMobile())
                .message(order.getMessage())
                .status(Order.NEW)
                .creatorId(order.getCreatorId()).creatorName(order.getCreatorName()).gmtCreate(order.getGmtCreate())
                .build();
        orderPoMapper.insert(orderPo);
        order.setId(orderPo.getId());
    }

    /**
     * 查找订单
     *
     * @param id 订单id
     */
    public Order findById(Long id) {
        logger.debug("findOrderById: id = {}", id);
        if (null == id) {
            throw new BusinessException(ReturnNo.PARAMETER_MISSED, ReturnNo.PARAMETER_MISSED.getMessage());
        }

        Order ret;
        String key = String.format(KEY, id);
        if (redisUtil.hasKey(key)) {
            logger.info("redis has key: {}", key);
            ret = (Order) redisUtil.get(key);
            this.setBo(ret);
        } else {
            OrderPo po = orderPoMapper.selectByPrimaryKey(id);
            if (po == null) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "订单", id));
            }
            logger.info("find order po by id: {}", po);
            ret = this.getBo(po, Optional.of(key));
        }
        return ret;
    }

    private void andCustomerIdAndOrderSnAndBetweenTime(OrderPoExample.Criteria criteria, Long customerId, String orderSn, LocalDateTime beginTime, LocalDateTime endTime) {
        if (null != orderSn) {
            logger.debug("Retrieve orders and orderSn = {}", orderSn);
            criteria.andOrderSnEqualTo(orderSn);
        }

        if (null != customerId) {
            logger.debug("Retrieve orders and customerId = {}", customerId);
            criteria.andCustomerIdEqualTo(customerId);
        }

        if (null != beginTime && null == endTime) {
            logger.debug("Retrieve orders and beginTime = {}", beginTime);
            criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        } else if (null == beginTime && null != endTime) {
            logger.debug("Retrieve orders and endTime = {}", endTime);
            criteria.andGmtCreateLessThanOrEqualTo(endTime);
        } else if (null != beginTime && beginTime.isBefore(endTime)) {
            logger.debug("Retrieve orders and beginTime = {} and endTime = {}", beginTime, endTime);
            criteria.andGmtCreateBetween(beginTime, endTime);
        } else if (null != beginTime && beginTime.isAfter(endTime)) {
            logger.debug("Retrieve orders 开始时间晚于结束时间 beginTime = {} and endTime = {}", beginTime, endTime);
            throw new BusinessException(ReturnNo.LATE_BEGINTIME);
        }
    }

    private PageInfo<Order> andStartPage(OrderPoExample example, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize, false);
        logger.debug("Retrieve orders by page = {} and pageSize = {}", page, pageSize);
        List<OrderPo> poList = orderPoMapper.selectByExample(example);
        if (poList.size() > 0) {
            logger.info("find orderPo list: {}", poList);
            List<Order> ret = poList.stream().map(item -> getBo(item, Optional.of(String.format(KEY, item.getId())))).collect(Collectors.toList());
            return new PageInfo<>(ret);
        } else {
            logger.info("find empty orderPo list");
            return new PageInfo<>(new ArrayList<>());
        }
    }


    /**
     * 买家查询名下订单
     *
     * @param customerId 买家id
     * @param orderSn    订单编号
     * @param status     订单大状态
     * @param beginTime  订单创建时间起始
     * @param endTime    订单创建时间终止
     * @param page       页码
     * @param pageSize   页大小
     * @return 订单列表
     */
    public PageInfo<Order> retrieveOrdersByCustomerIdAndOrderSnAndBetweenSuccessTime(Long customerId, String orderSn, Integer status, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize) {
        OrderPoExample example = new OrderPoExample();
        OrderPoExample.Criteria criteria = example.createCriteria();
        if (null != status) {
            logger.debug("Retrieve orders and status = {}", status);
            criteria.andStatusBetween(status, status + 99);
        }
        andCustomerIdAndOrderSnAndBetweenTime(criteria, customerId, orderSn, beginTime, endTime);
        return this.andStartPage(example, page, pageSize);
    }

    /**
     * 更新对象
     *
     * @param order
     * @param user
     * @throws RuntimeException
     */
    public void saveById(Order order, UserDto user) throws RuntimeException {
        OrderPo po = OrderPo.builder()
                .id(order.getId()).shopId(order.getShopId()).orderSn(order.getOrderSn())
                .status(order.getStatus()).customerId(order.getCustomerId()).pid(order.getPid())
                .packageId(order.getPackageId()).expressFee(order.getExpressFee())
                .address(order.getConsignee().getAddress()).consignee(order.getConsignee().getName())
                .mobile(order.getConsignee().getMobile()).regionId(order.getConsignee().getRegionId())
                .discountPrice(order.getDiscountPrice()).originPrice(order.getOriginPrice()).point(order.getPoint())
                .message(order.getMessage())
                .build();
        if (null != user) {
            putUserFields(po, "modifier", user);
            putGmtFields(po, "modified");
        }
        int ret = orderPoMapper.updateByPrimaryKeySelective(po);
        logger.debug("ret :" + ret);
        if (0 == ret) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
        }
    }


    public PageInfo<Order> retrieveOrdersByShopIdAndCustomerIdAndOrderSnAndBetweenTime(Long shopId, Long customerId, String orderSn, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize) {
        OrderPoExample example = new OrderPoExample();
        OrderPoExample.Criteria criteria = example.createCriteria();
        if (null != shopId) {
            logger.debug("Retrieve orders and shopId = {}", shopId);
            criteria.andShopIdEqualTo(shopId);
        }
        andCustomerIdAndOrderSnAndBetweenTime(criteria, customerId, orderSn, beginTime, endTime);
        return andStartPage(example, page, pageSize);
    }

    /**
     * 根据订单编号查找订单
     *
     * @param orderSn 订单编号
     * @return Order Bo
     */
    public Order findByOrderSn(String orderSn) {
        logger.debug("findOrderByOrderSn: order = {}", orderSn);
        OrderPoExample example = new OrderPoExample();
        OrderPoExample.Criteria criteria = example.createCriteria();
        criteria.andOrderSnEqualTo(orderSn);
        List<OrderPo> poList = orderPoMapper.selectByExample(example);
        if (poList.size() == 0) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "订单编号对应订单", orderSn));
        }
        OrderPo po = poList.get(0);
        return getBo(po, Optional.of(String.format(KEY, po.getId())));
    }
}

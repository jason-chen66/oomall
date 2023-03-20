//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.order.controller.vo.ConsigneeVo;
import cn.edu.xmu.oomall.order.controller.vo.DeliverVo;
import cn.edu.xmu.oomall.order.dao.OrderDao;
import cn.edu.xmu.oomall.order.dao.OrderItemDao;
import cn.edu.xmu.oomall.order.dao.OrderPaymentDao;
import cn.edu.xmu.oomall.order.dao.OrderRefundDao;
import cn.edu.xmu.oomall.order.dao.openfeign.*;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.*;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.CouponDto;
import cn.edu.xmu.oomall.order.dao.openfeign.vo.DiscountVo;
import cn.edu.xmu.oomall.order.dao.openfeign.vo.PaymentVo;
import cn.edu.xmu.oomall.order.dao.openfeign.vo.RefundVo;
import cn.edu.xmu.oomall.order.service.dto.*;
import cn.edu.xmu.oomall.order.service.dto.SimplePackageDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order extends OOMallObject implements Serializable {

    @ToString.Exclude
    @JsonIgnore
    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    /**
     * 待付款（大状态）
     */
    @ToString.Exclude
    @JsonIgnore
    public final static Integer UNPAID = 100;

    /**
     * 新订单
     */
    @ToString.Exclude
    @JsonIgnore
    public final static Integer NEW = 101;

    /**
     * 待支付尾款
     */
    @ToString.Exclude
    @JsonIgnore
    public final static Integer BALANCE_UNPAID = 102;

    /**
     * 待收货（大状态）
     */
    @ToString.Exclude
    @JsonIgnore
    public final static Integer NOT_RECEIVED = 200;

    /**
     * 已付款
     */
    @ToString.Exclude
    @JsonIgnore
    public final static Integer PAID = 201;

    /**
     * 待成团
     */
    @ToString.Exclude
    @JsonIgnore
    public final static Integer NOT_GROUPON = 202;

    /**
     * 待发货
     */
    public final static Integer NOT_SENT = 203;

    /**
     * 已发货
     */
    @ToString.Exclude
    @JsonIgnore
    public final static Integer SENT = 204;
    /**
     * 已完成
     */
    @ToString.Exclude
    @JsonIgnore
    public final static Integer SUCCESS = 300;

    /**
     * 已取消（大状态）
     */
    @ToString.Exclude
    @JsonIgnore
    public final static Integer CANCELED = 400;

    /**
     * 待退款
     */
    @ToString.Exclude
    @JsonIgnore
    public final static Integer NOT_REFUNDED = 401;

    /**
     * 已取消(已退款)
     */
    @ToString.Exclude
    @JsonIgnore
    public final static Integer REFUNDED = 402;

    /**
     * 状态和名称的对应
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Map<Integer, String> STATUSNAMES = new HashMap() {
        {
            put(NEW, "新订单");
            put(SUCCESS, "已完成");
            put(BALANCE_UNPAID, "待支付尾款");
            put(UNPAID, "待付款");
            put(NOT_RECEIVED, "待收货");
            put(PAID, "已付款");
            put(NOT_GROUPON, "待成团");
            put(NOT_SENT, "待发货");
            put(SENT, "已发货");
            put(CANCELED, "已取消");
            put(NOT_REFUNDED, "待退款");
            put(REFUNDED, "已退款");
        }
    };

    @JsonIgnore
    @Setter
    private OrderDao orderDao;

    @JsonIgnore
    @Setter
    private OrderItemDao orderItemDao;

    @JsonIgnore
    @Setter
    private OrderPaymentDao orderPaymentDao;

    @JsonIgnore
    @Setter
    private OrderRefundDao orderRefundDao;

    @JsonIgnore
    @Setter
    private PaymentDao paymentDao;

    @JsonIgnore
    @Setter
    private GoodsDao goodsDao;

    @Setter
    @Getter
    private String orderSn;

    @Setter
    @Getter
    private Long pid;

    @Setter
    @Getter
    private String message;

    @Setter
    private Long activityId;

    @Setter
    @Getter
    private Integer status;

    @Setter
    @Getter
    private Long point;

    @Setter
    @Getter
    private Long originPrice;

    @Setter
    @Getter
    private Long discountPrice;

    @Setter
    @Getter
    private Long expressFee;

    @Setter
    @Getter
    private ConsigneeDto consignee;

    @Setter
    private List<OrderItem> orderItems;

    @Builder
    public Order(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified,
                 String orderSn, Long pid, ConsigneeDto consignee, String message,
                 Long activityId, Integer status, Long originPrice, Long discountPrice, Long expressFee,
                 Long customerId, Long shopId, Long packageId, List<OrderItem> orderItems, Long point) {

        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.orderSn = orderSn;
        this.pid = pid;
        this.message = message;
        this.activityId = activityId;
        this.status = status;
        this.originPrice = originPrice;
        this.discountPrice = discountPrice;
        this.point = point;
        this.expressFee = expressFee;
        this.customerId = customerId;
        this.shopId = shopId;
        this.packageId = packageId;
        this.orderItems = orderItems;
        this.consignee = consignee;
    }

    public OrderInfoDto gotOrderInfo() throws BusinessException {
        getCustomer();
        getShop();
        getPack();
        return OrderInfoDto.builder()
                .id(id)
                .orderSn(orderSn)
                .customer(SimpleCustomerDto.builder().id(customer.getId()).name(customer.getName()).build())
                .shop(SimpleShopDto.builder().id(shop.getId()).name(shop.getName()).type(shop.getType()).status(shop.getStatus()).build())
                .pid(pid)
                .status(status)
                .gmtCreate(gmtCreate)
                .gmtModified(gmtModified)
                .originPrice(originPrice)
                .disCountPrice(discountPrice)
                .expressFee(expressFee)
                .message(message)
                .consignee(ConsigneeDto.builder().build())
                .pack(SimplePackageDto.builder().id(pack.getId()).billCode(pack.getBillCode()).build())
                .orderItems(orderItems.stream().map(item -> OrderItemDto.builder()
                        .orderId(item.getOrderId())
                        .name(item.getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .discountPrice(item.getDiscountPrice())
                        .activity(item.getActivity())
                        .coupon(item.findCouponAndActivity())
                        .build()
                ).collect(Collectors.toList()))
                .build();
    }

    /**
     * 获得当前状态名称
     *
     * @return 状态名
     */
    public String getStatusName() {
        return STATUSNAMES.get(this.status);
    }

    /**
     * 订单允许的状态迁移
     */
    private static final Map<Integer, Set<Integer>> toStatus = new HashMap<>() {
        {
            put(NEW, new HashSet<>() {
                {
                    add(PAID);
                    add(BALANCE_UNPAID);
                    add(REFUNDED);
                }
            });

            put(PAID, new HashSet<>() {
                {
                    add(NOT_SENT);
                    add(SUCCESS);
                    add(NOT_REFUNDED);
                }
            });
            put(BALANCE_UNPAID, new HashSet<>() {
                {
                    add(PAID);
                    add(NOT_REFUNDED);
                    add(REFUNDED);
                }
            });
            put(NOT_SENT, new HashSet<>() {
                {
                    add(SENT);
                    add(NOT_REFUNDED);
                }
            });
            put(SENT, new HashSet<>() {
                {
                    add(NOT_REFUNDED);
                    add(SUCCESS);
                }
            });
            put(NOT_REFUNDED, new HashSet<>() {
                {
                    add(REFUNDED);
                }
            });
            put(REFUNDED, new HashSet<>() {
                {
                    add(CANCELED);
                }
            });
        }
    };

    /**
     * 是否允许状态迁移
     *
     * @param status 目标状态
     * @return 是否允许
     */
    public boolean allowStatus(Integer status) {
        boolean ret = false;

        if (null != status && null != this.getStatus()) {
            Set<Integer> allowStatusSet = toStatus.get(this.getStatus());
            if (null != allowStatusSet) {
                ret = allowStatusSet.contains(status);
            }
        }
        return ret;
    }

    /**
     * 获取订单明细
     *
     * @return 订单明细
     */
    public List<OrderItem> findOrderItems() throws BusinessException {
        List<OrderItem> orderItems = orderItemDao.retrieveOrderItemsByOrderId(this.id);

        // 若非父订单，且无订单明细应抛出错误
        if (0 != pid && null == orderItems) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.IMG_FORMAT_ERROR.getMessage()));
        }
        this.orderItems = orderItems;
        return orderItems;
    }

    /**
     * 获取订单支付
     *
     * @return 订单支付
     */
    public List<OrderPayment> getOrderPayments() throws BusinessException {
        return orderPaymentDao.retrieveOrderPaymentsByOrderId(this.id);
    }

    /**
     * 查询订单退款
     *
     * @return 订单退款结果
     */
    public List<RefundCheckDto> checkOrderRefunds() throws BusinessException {
        List<OrderRefund> refunds = orderRefundDao.retrieveOrderRefundsByOrderId(this.id);
        List<RefundCheckDto> rets = new ArrayList<>();
        for (OrderRefund refund : refunds) {
            RefundCheckDto ret = paymentDao.findRefund(this.shop.getId(), refund.getRefundId()).getData();
            rets.add(ret);
        }
        if (rets.size() != refunds.size()) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, ReturnNo.INTERNAL_SERVER_ERR.getMessage());
        }
        return rets;
    }

    /**
     * 计算待支付金额
     */
    public Long calPayAmount() {
        Long price = originPrice - discountPrice + expressFee - point / 100;
        return price;
    }

    /**
     * 对指定paymentId创建退款需要的vo对象
     */
    public RefundVo createRefund(Long paymentId) throws BusinessException {
        if (orderItems != null && paymentId != null) {
            //1.查询并计算每个商品的佣金 2.sum()计算
            Long divAmount = orderItems.stream().mapToLong(orderItem -> {
                return orderItem.getDivAmountPerItem(this.shopId);
            }).sum();
            RefundVo vo = new RefundVo();
            vo.setAmount(this.calPayAmount());
            vo.setDivAmount(divAmount);
            return vo;
        }
        return null;
    }

    /**
     * 验证优惠券，若验证失败，应该返回null
     *
     * @param coupon
     * @return
     */
    public CouponDto validateCoupon(Long coupon) {
        InternalReturnObject<CouponDto> ret = this.customerDao.findCouponById(0L, coupon);
        if (ret.getErrno() == ReturnNo.OK.getErrNo()) {
            return ret.getData();
        }
        return null;
    }

    /**
     * 创建支付需要的vo对象
     */
    public PaymentVo createPayment(Long shopChannel, Long point, List<Long> coupons) throws BusinessException {
        //设置总积点,查询订单名下所有明细
        this.point = point;
        this.findOrderItems();
        if (orderItems == null)
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "orderItems"));

        //对每一个coupon进行调用customer模块进行验证，若不可用则不计入支付进行的优惠计算,通过验证的优惠券根据活动Id去重，保证优惠券使用不叠加
        List<CouponDto> validates = coupons.stream().map(this::validateCoupon).filter(Objects::nonNull).collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CouponDto::getActivityId))), ArrayList::new));

        //通过coupon.actId,orderItem.actId求交集，转换成Map<ActId,List<OrderItem>>,计算同一活动中一批明细的优惠
        Map<Long, List<OrderItem>> map = orderItems.stream().filter(item -> validates.stream()
                .anyMatch(couponDto -> Objects.equals(item.getActId(), couponDto.getActivityId()))).collect(Collectors.groupingBy(OrderItem::getActId));

        //分批计算优惠，设置优惠券和优惠价格
        map.keySet().stream().forEach(key -> {
            List<OrderItem> values = map.get(key);
            List<DiscountDto> ret = calculateDiscount(key, values);

            // 先找到key对应的orderItem，然后对下面的item逐一设置其修改后的折扣
            orderItems.stream().filter(item -> item.getActId().equals(key)).forEach(
                    orderItem -> {
                        DiscountDto discount =  ret.stream().filter(discountDto -> discountDto.getId().equals(orderItem.getOnsaleId())).findAny().get();
                        CouponDto couponDto = validates.stream().filter(item -> item.getActivityId().equals(orderItem.getActId())).findAny().get();
                        orderItem.setDiscountPrice(discount.getDiscount());
                        orderItem.setCouponId(couponDto.getId());
                    }
            );
        });

        //1.按实际支付比例分摊积点，2.查询并计算每个商品的佣金 3.sum()计算
        Long divAmount = orderItems.stream().mapToLong(orderItem -> {
                    Long pointPerItem = orderItem.calPayPrice() / (this.originPrice - this.discountPrice) * point;
                    orderItem.setPoint(pointPerItem);     //set明细积点
                    return orderItem.getDivAmountPerItem(this.shop.getId());
                })
                .sum();

        //返回支付vo,service发出消息进行支付
        return new PaymentVo(LocalDateTime.now(), LocalDateTime.now().plusMinutes(30), shopChannel, this.calPayAmount(), divAmount, "customer");
    }

    /**
     * 计算同一活动下一批明细的优惠
     *
     * @param actId
     * @param orderItems
     * @return
     */
    public List<DiscountDto> calculateDiscount(Long actId, List<OrderItem> orderItems) {
        if (actId != null) {
            List<DiscountVo> vo = orderItems.stream().map(item ->
                    DiscountVo.builder().id(item.getOnsaleId()).quantity(item.getQuantity()).build()).collect(Collectors.toList());
            InternalReturnObject<List<DiscountDto>> ret = goodsDao.calculateDiscount(actId, vo);
            if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())) {
                return ret.getData();
            } else {
                logger.debug("getShop: " + ret.getErrmsg());
            }
        } else {
            throw new BusinessException(ReturnNo.PARAMETER_MISSED, ReturnNo.PARAMETER_MISSED.getMessage());
        }
        return null;
    }

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private CustomerDao customerDao;

    @ToString.Exclude
    @JsonIgnore
    private CustomerDto customer;

    @Setter
    @Getter
    private Long customerId;

    public CustomerDto getCustomer() {
        if (null != customerId && null == customer) {
            InternalReturnObject<CustomerDto> ret = customerDao.findCustomer(0L, customerId);
            if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())) {
                customer = ret.getData();
            } else {
                logger.debug("getCustomer: " + ret.getErrmsg());
            }
        }
        return customer;
    }


    @Setter
    @JsonIgnore
    @ToString.Exclude
    private ShopDao shopDao;

    @ToString.Exclude
    @JsonIgnore
    private ShopDto shop;

    @Setter
    @Getter
    private Long shopId;

    public ShopDto getShop() {
        if (null != shopId && null == shop) {
            InternalReturnObject<ShopDto> ret = shopDao.findShop(shopId);
            if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())) {
                shop = ret.getData();
            } else {
                logger.debug("getShop: " + ret.getErrmsg());
            }
        }
        return shop;
    }

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private FreightDao freightDao;

    @ToString.Exclude
    @JsonIgnore
    private PackageDto pack;

    @Setter
    @Getter
    private Long packageId;

    public PackageDto getPack() {
        if (null != packageId && null == pack) {
            InternalReturnObject<PackageDto> ret = freightDao.findPackage(packageId);
            if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())) {
                pack = ret.getData();
            } else {
                logger.debug("getPack: " + ret.getErrmsg());
            }
        }
        return pack;
    }

    /**
     * 订单发货
     */
    public void createPackage() throws BusinessException {
        ConsigneeDto delivery = consignee;
        DeliverVo vo = new DeliverVo();
        ConsigneeVo deliveryVo = new ConsigneeVo(delivery.getName(), delivery.getAddress(), delivery.getRegionId(), delivery.getMobile());

        vo.setDelivery(deliveryVo);
        vo.setShopLogisticId(0);
        this.setConsignee(ConsigneeDto.builder().name(delivery.getName()).address(delivery.getAddress()).regionId(delivery.getRegionId()).mobile(delivery.getMobile()).build());

        if (this.shopId != null) {
            cn.edu.xmu.oomall.order.dao.openfeign.dto.SimplePackageDto dto = freightDao.createPackage(shopId, vo).getData();
            if (dto == null) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "顾客", id));
            }
            this.pack.setId(dto.getId());
            this.pack.setBillCode(dto.getBillCode());
        } else {
            throw new BusinessException(ReturnNo.PARAMETER_MISSED);
        }
    }

}

package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.order.OrderTestApplication;
import cn.edu.xmu.oomall.order.dao.OrderDao;
import cn.edu.xmu.oomall.order.dao.OrderItemDao;
import cn.edu.xmu.oomall.order.dao.OrderPaymentDao;
import cn.edu.xmu.oomall.order.dao.OrderRefundDao;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import cn.edu.xmu.oomall.order.dao.bo.OrderPayment;
import cn.edu.xmu.oomall.order.dao.bo.OrderRefund;
import cn.edu.xmu.oomall.order.dao.openfeign.GoodsDao;
import cn.edu.xmu.oomall.order.dao.openfeign.PaymentDao;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.*;
import cn.edu.xmu.oomall.order.mapper.generator.OrderItemPoMapper;
import cn.edu.xmu.oomall.order.mapper.generator.OrderPaymentPoMapper;
import cn.edu.xmu.oomall.order.mapper.generator.OrderPoMapper;
import cn.edu.xmu.oomall.order.mapper.generator.OrderRefundPoMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = OrderTestApplication.class)
@Transactional
public class OrderListenerServiceTest {

    @MockBean
    private RedisUtil redisUtil;

   /* @MockBean
    private OrderProcessor orderProcessor;

    @MockBean(name = "NormalProcessor")
    private OrderProcessor normalProcessor;

    @MockBean(name = "GrouponProcessor")
    private OrderProcessor grouponProcessor;

    @MockBean(name = "AdvSaleProcessor")
    private OrderProcessor advSaleProcessor;*/
   private cn.edu.xmu.oomall.order.service.OrderListenerService orderListenerService;
    @Autowired
    public OrderListenerServiceTest(cn.edu.xmu.oomall.order.service.OrderListenerService orderListenerService) {
        this.orderListenerService = orderListenerService;
    }


    @MockBean
    private OrderDao orderDao;

    @MockBean
    private OrderItemDao orderItemDao;
    @MockBean
    private OrderPaymentDao orderPaymentDao;

    @MockBean
    private OrderRefundDao orderRefundDao;


    @MockBean
    private GoodsDao goodsDao;

    @MockBean
    private PaymentDao paymentDao;

    @MockBean
    private OrderPaymentPoMapper orderPaymentPoMapper;

    @MockBean
    private OrderPoMapper orderPoMapper;

    @MockBean
    private OrderItemPoMapper orderItemPoMapper;

    @MockBean
    private OrderRefundPoMapper orderRefundPoMapper;


    private OrderPayment order31500Payment1 = OrderPayment.builder().id(1L).orderId(31500L).paymentId(1L).creatorId(1L).gmtCreate(LocalDateTime.parse("2016-10-23T12:17:00")).build();
    private OrderPayment order31500Payment2 = OrderPayment.builder().id(2L).orderId(31500L).paymentId(2L).creatorId(1L).gmtCreate(LocalDateTime.parse("2016-10-25T12:17:00")).build();

    private OrderRefund order31500Refund1 = OrderRefund.builder().id(1L).orderId(31500L).refundId(1L).creatorId(1L).gmtCreate(LocalDateTime.parse("2016-10-23T12:17:00")).build();
    private OrderRefund order31500Refund2 = OrderRefund.builder().id(2L).orderId(31500L).refundId(2L).creatorId(1L).gmtCreate(LocalDateTime.parse("2016-10-23T12:17:00")).build();
    RefundResultDto r1 = RefundResultDto.builder().id(1L).build();
    RefundResultDto r2 = RefundResultDto.builder().id(2L).build();
    InternalReturnObject<RefundResultDto> retr1 = new InternalReturnObject<RefundResultDto>(r1);
    InternalReturnObject<RefundResultDto> retr2 = new InternalReturnObject<RefundResultDto>(r2);
    private Order order31500 = Order.builder().id(31500L).shopId(1L).orderSn("2018120725423").expressFee(707L).shopId(1L).customerId(1L).creatorId(1L).gmtCreate(LocalDateTime.parse("2016-10-23T12:17:00")).build();

    private UserDto user = new UserDto();

    private List<OrderItem> orderItems = new ArrayList<>();
    private List<OrderPayment> pays = new ArrayList<>();
    private List<OrderRefund> refunds = new ArrayList<>();
    private OrderItem orderItem1 = OrderItem.builder().id(50000L).orderId(31500L).price(320L).quantity(3).onsaleId(1L).build();
    private OnsaleDto o1 = OnsaleDto.builder().id(1L).product(new IdNameDto(1L,"洗发水1")).build();
    private ProductDto p1 = ProductDto.builder().id(1L).commissionRatio(10).build();
    private InternalReturnObject<OnsaleDto> reto1 = new InternalReturnObject<>(o1);
    private InternalReturnObject<ProductDto> retp1 = new InternalReturnObject<>(p1);

    private OrderItem orderItem2 = OrderItem.builder().id(50001L).orderId(31500L).price(990L).quantity(2).onsaleId(2L).build();
    private OnsaleDto o2 = OnsaleDto.builder().id(2L).product(new IdNameDto(2L,"洗发水2")).build();
    private ProductDto p2 = ProductDto.builder().id(2L).commissionRatio(18).build();
    private InternalReturnObject<OnsaleDto> reto2 = new InternalReturnObject<>(o2);
    private InternalReturnObject<ProductDto> retp2 = new InternalReturnObject<>(p2);

    private OrderItem orderItem3 = OrderItem.builder().id(50002L).orderId(31500L).price(1990L).quantity(1).onsaleId(3L).build();
    private OnsaleDto o3 = OnsaleDto.builder().id(3L).product(new IdNameDto(3L,"洗发水3")).build();
    private ProductDto p3 = ProductDto.builder().id(3L).commissionRatio(17).build();
    private InternalReturnObject<OnsaleDto> reto3 = new InternalReturnObject<>(o3);
    private InternalReturnObject<ProductDto> retp3 = new InternalReturnObject<>(p3);


    @Test
    public void actDeleteOrderTest1() {
        orderItems.add(orderItem1);
        pays.add(order31500Payment1);
        refunds.add(order31500Refund1);
        order31500.setOrderItems(orderItems);
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);

        Mockito.when(orderItemDao.findByAct(Mockito.anyLong())).thenReturn(orderItems);
        Mockito.when(orderDao.findById(Mockito.anyLong())).thenReturn(order31500);
        Mockito.when(orderItemDao.retrieveOrderItemsByOrderId(Mockito.anyLong())).thenReturn(orderItems);
        Mockito.when(orderPaymentDao.retrieveOrderPaymentsByOrderId(Mockito.anyLong())).thenReturn(pays);
        Mockito.when(goodsDao.getOnsaleById(Mockito.any(),Mockito.anyLong())).thenReturn(reto1);
        Mockito.when(goodsDao.getProductByIdAndShopId(Mockito.any(),Mockito.anyLong())).thenReturn(retp1);
        Mockito.when(paymentDao.createRefund(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(retr1);
        orderListenerService.actFailOrder(Mockito.anyLong(),user);
    }

    @Test
    public void NormalSaveOrderPayment() {
        /*
        logger.debug ->{
            item1 point = 19
            item2 point = 41
            item3 point = 39
            order divAmount = 677
            order amount 3453
         }
         */
        user.setId(1L);
        user.setName("张三");
        orderItem1.setDiscountPrice(100L);
        orderItem2.setDiscountPrice(200L);
        orderItem3.setDiscountPrice(300L);
        orderItem1.setCouponId(100L);
        orderItem2.setCouponId(200L);
        orderItem3.setCouponId(300L);
        orderItem1.setPoint(19L);
        orderItem2.setPoint(41L);
        orderItem3.setPoint(39L);
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);
        orderItems.add(orderItem3);
        order31500.setOrderItems(orderItems);
        o1.setType(OnsaleDto.NORMAL);
        order31500.setStatus(Order.NEW);

        Mockito.when(goodsDao.getOnsaleById(Mockito.any(),Mockito.anyLong())).thenReturn(new InternalReturnObject<>(o1));
        Mockito.when(orderPaymentPoMapper.insertSelective(Mockito.any())).thenReturn(1);
        Mockito.when(orderPoMapper.updateByPrimaryKeySelective(Mockito.any())).thenReturn(1);
        Mockito.when(orderItemPoMapper.updateByPrimaryKeySelective(Mockito.any())).thenReturn(1);

        orderListenerService.saveOrderPayment(order31500,order31500Payment1,user);
    }
    @Test
    public void NormalSaveOrderPaymentInsertFail() {
        order31500.setStatus(Order.NEW);
        o1.setType(OnsaleDto.NORMAL);
        Mockito.when(goodsDao.getOnsaleById(Mockito.anyLong(),Mockito.anyLong())).thenReturn(new InternalReturnObject<>(o1));
        assertThrows(BusinessException.class,()->orderListenerService.saveOrderPayment(Mockito.any(),null,Mockito.any()));
    }
    @Test
    public void NormalSaveOrderPaymentStatusNotAllow() {
        user.setId(1L);
        user.setName("张三");
        orderItem1.setDiscountPrice(100L);
        orderItem2.setDiscountPrice(200L);
        orderItem3.setDiscountPrice(300L);
        orderItem1.setCouponId(100L);
        orderItem2.setCouponId(200L);
        orderItem3.setCouponId(300L);
        orderItem1.setPoint(19L);
        orderItem2.setPoint(41L);
        orderItem3.setPoint(39L);
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);
        orderItems.add(orderItem3);
        order31500.setStatus(Order.REFUNDED);
        order31500.setOrderItems(orderItems);
        o1.setType(OnsaleDto.NORMAL);

        Mockito.when(goodsDao.getOnsaleById(Mockito.any(),Mockito.anyLong())).thenReturn(new InternalReturnObject<>(o1));

        try{
            orderListenerService.saveOrderPayment(order31500,order31500Payment1,user);
        }catch (BusinessException e){
            assertEquals(e.getErrno(), ReturnNo.STATENOTALLOW);
        }
    }
    @Test
    public void GrouponSaveOrderPayment() {
        user.setId(1L);
        user.setName("张三");
        order31500.setStatus(Order.NEW);
        orderItem1.setDiscountPrice(100L);
        orderItems.add(orderItem1);
        order31500.setOrderItems(orderItems);
        o1.setType(OnsaleDto.GROUPON);

        Mockito.when(goodsDao.getOnsaleById(Mockito.any(),Mockito.anyLong())).thenReturn(new InternalReturnObject<>(o1));
        Mockito.when(orderPaymentPoMapper.insertSelective(Mockito.any())).thenReturn(1);
        Mockito.when(orderPoMapper.updateByPrimaryKeySelective(Mockito.any())).thenReturn(1);
        Mockito.when(orderItemPoMapper.updateByPrimaryKeySelective(Mockito.any())).thenReturn(1);

        orderListenerService.saveOrderPayment(order31500,order31500Payment1,user);
    }
    @Test
    public void GrouponSaveOrderPaymentInsertFail() {
        order31500.setStatus(Order.NEW);
        o1.setType(OnsaleDto.GROUPON);
        Mockito.when(goodsDao.getOnsaleById(Mockito.any(),Mockito.anyLong())).thenReturn(new InternalReturnObject<>(o1));
        assertThrows(BusinessException.class,()->orderListenerService.saveOrderPayment(Mockito.any(),null,Mockito.any()));
    }
    @Test
    public void GrouponSaveOrderPaymentStatusNotAllow() {
        user.setId(1L);
        user.setName("张三");
        orderItem1.setDiscountPrice(100L);
        orderItems.add(orderItem1);
        order31500.setStatus(Order.REFUNDED);
        order31500.setOrderItems(orderItems);
        o1.setType(OnsaleDto.GROUPON);

        Mockito.when(goodsDao.getOnsaleById(Mockito.any(),Mockito.anyLong())).thenReturn(new InternalReturnObject<>(o1));
        try{
            orderListenerService.saveOrderPayment(order31500,order31500Payment1,user);
        }catch (BusinessException e){
            assertEquals(e.getErrno(), ReturnNo.INTERNAL_SERVER_ERR);
        }
    }
    @Test
    public void AdvSaleSaveOrderPaymentAdvPay() {
        user.setId(1L);
        user.setName("张三");
        orderItems.add(orderItem1);
        order31500.setOrderItems(orderItems);
        o1.setType(OnsaleDto.ADVSALE);
        order31500.setStatus(Order.NEW);

        Mockito.when(goodsDao.getOnsaleById(Mockito.any(),Mockito.anyLong())).thenReturn(new InternalReturnObject<>(o1));
        Mockito.when(orderPaymentPoMapper.insertSelective(Mockito.any())).thenReturn(1);
        Mockito.when(orderPoMapper.updateByPrimaryKeySelective(Mockito.any())).thenReturn(1);
        Mockito.when(orderItemPoMapper.updateByPrimaryKeySelective(Mockito.any())).thenReturn(1);

        orderListenerService.saveOrderPayment(order31500,order31500Payment1,user);
    }
    @Test
    public void AdvSaleSaveOrderPaymentBalancePay() {
        user.setId(1L);
        user.setName("张三");
        orderItems.add(orderItem1);
        order31500.setOrderItems(orderItems);
        o1.setType(OnsaleDto.ADVSALE);
        order31500.setStatus(Order.BALANCE_UNPAID);

        Mockito.when(goodsDao.getOnsaleById(Mockito.any(),Mockito.anyLong())).thenReturn(new InternalReturnObject<>(o1));
        Mockito.when(orderPaymentPoMapper.insertSelective(Mockito.any())).thenReturn(1);
        Mockito.when(orderPoMapper.updateByPrimaryKeySelective(Mockito.any())).thenReturn(1);
        Mockito.when(orderItemPoMapper.updateByPrimaryKeySelective(Mockito.any())).thenReturn(1);

        orderListenerService.saveOrderPayment(order31500,order31500Payment1,user);
    }
    @Test
    public void AdvSaleSaveOrderPaymentInsertFail() {
        order31500.setStatus(Order.NEW);
        o1.setType(OnsaleDto.ADVSALE);
        Mockito.when(goodsDao.getOnsaleById(Mockito.any(),Mockito.anyLong())).thenReturn(new InternalReturnObject<>(o1));
        assertThrows(BusinessException.class,()->orderListenerService.saveOrderPayment(Mockito.any(),null,Mockito.any()));
    }
    @Test
    public void AdvSaleSaveOrderPaymentStatusNotAllow() {
        user.setId(1L);
        user.setName("张三");
        orderItem1.setDiscountPrice(100L);
        orderItems.add(orderItem1);
        order31500.setStatus(Order.REFUNDED);
        order31500.setOrderItems(orderItems);
        o1.setType(OnsaleDto.ADVSALE);

        Mockito.when(goodsDao.getOnsaleById(Mockito.any(),Mockito.anyLong())).thenReturn(new InternalReturnObject<>(o1));

        try{
            orderListenerService.saveOrderPayment(order31500,order31500Payment1,user);
        }catch (BusinessException e){
            assertEquals(e.getErrno(), ReturnNo.STATENOTALLOW);
        }
    }
    @Test
    public void saveOrderRefundTest1() {
        user.setId(1L);
        user.setName("张三");
        orderItem1.setDiscountPrice(100L);
        orderItem2.setDiscountPrice(200L);
        orderItem3.setDiscountPrice(300L);
        orderItem1.setCouponId(100L);
        orderItem2.setCouponId(200L);
        orderItem3.setCouponId(300L);
        orderItem1.setPoint(19L);
        orderItem2.setPoint(41L);
        orderItem3.setPoint(39L);
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);
        orderItems.add(orderItem3);
        order31500.setOrderItems(orderItems);
        o1.setType(OnsaleDto.NORMAL);
        order31500.setStatus(Order.NOT_SENT);

        Mockito.when(orderRefundPoMapper.insertSelective(Mockito.any())).thenReturn(1);
        Mockito.when(orderPoMapper.updateByPrimaryKeySelective(Mockito.any())).thenReturn(1);

        orderListenerService.saveOrderRefund(order31500,order31500Refund1,user);
    }
}

package cn.edu.xmu.oomall.order.controller;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.order.OrderTestApplication;
import cn.edu.xmu.oomall.order.controller.vo.ShopOrderVo;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.openfeign.*;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.*;
import cn.edu.xmu.oomall.order.mapper.generator.OrderPaymentPoMapper;
import cn.edu.xmu.oomall.order.mapper.generator.OrderPoMapper;
import cn.edu.xmu.oomall.order.mapper.generator.OrderRefundPoMapper;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderPaymentPo;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderPo;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderRefundPo;
import cn.edu.xmu.oomall.order.service.dto.ConsigneeDto;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@SpringBootTest(classes = OrderTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class AdminOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static String adminToken;

    @MockBean
    private OrderPoMapper orderPoMapper;

    @MockBean
    private FreightDao freightDao;

    @MockBean
    private OrderRefundPoMapper orderRefundPoMapper;

    @MockBean
    private PaymentDao paymentDao;

    @MockBean
    private ShopDao shopDao;

    @MockBean
    private CustomerDao customerDao;

    @MockBean
    private GoodsDao goodsDao;

    @MockBean
    private OrderPaymentPoMapper orderPaymentPoMapper;

    @MockBean
    private RedisUtil redisUtil;

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    private void mockFindPackage()
    {
        PackageDto packageDto = PackageDto.builder().id(1L).billCode("SF123").status(PackageDto.ON).build();
        InternalReturnObject<PackageDto> packageDtoInternalReturnObject = new InternalReturnObject<>();
        packageDtoInternalReturnObject.setData(packageDto);
        Mockito.when(freightDao.findPackage(Mockito.any())).thenReturn(packageDtoInternalReturnObject);
    }

    private void mockCreatePackage()
    {
        SimplePackageDto simplePackageDto = new SimplePackageDto();
        InternalReturnObject<SimplePackageDto> returnObject = new InternalReturnObject<>();
        returnObject.setData(simplePackageDto);
        Mockito.when(freightDao.createPackage(Mockito.any(),Mockito.any())).thenReturn(returnObject);
    }


    private void mockFindShop()
    {
        ShopDto shopDto = ShopDto.builder().id(1L).status(1)
                .consignee(ConsigneeDto.builder()
                        .name("李二蛋").mobile("4516548798").address("快乐校区520号").regionId(2L)
                        .build()).build();
        InternalReturnObject<ShopDto> returnObject = new InternalReturnObject<>();
        returnObject.setData(shopDto);
        Mockito.when(shopDao.findShop(Mockito.any())).thenReturn(returnObject);
    }

    private void mockFindCustomer()
    {
        CustomerDto customerDto = CustomerDto.builder().id(1L).name("贾顾客").build();
        InternalReturnObject<CustomerDto> returnObject = new InternalReturnObject<>();
        returnObject.setData(customerDto);
        Mockito.when(customerDao.findCustomer(Mockito.any(),Mockito.any())).thenReturn(returnObject);
    }

    private void mockFindOrderById(int status)
    {
        Mockito.when(orderPoMapper.selectByPrimaryKey(Mockito.any()))
                .thenReturn(OrderPo.builder()
                        .orderSn("1").customerId(1L).id(1L).packageId(1L).shopId(1L).status(status)
                        .consignee("王大柱").mobile("1234567890").regionId(1L).address("幸福大道幸福小区250号")
                        .message("message").pid(0L)
                        .originPrice(100L).discountPrice(80L).expressFee(1L).point(10L)
                        .build());
    }

    private void mockRetrieveOrder()
    {
        List<OrderPo> poList = List.of(
                OrderPo.builder()
                        .orderSn("1").customerId(1L).status(1)
                        .expressFee(1245L)
                        .id(1L)
                        .packageId(1L)
                        .shopId(1L)
                        .build()
        );
        Mockito.when(orderPoMapper.selectByExample(Mockito.any())).thenReturn(poList);
    }

    private void mockSaveOrder()
    {
        Mockito.when(orderPoMapper.updateByPrimaryKeySelective(Mockito.any())).thenReturn(1);
    }


    private void mockFindOrderRefund()
    {
        List<OrderRefundPo> pos = List.of(
                OrderRefundPo.builder()
                        .orderId(1L).id(1L).refundId(1L)
                        .build()
        );
        Mockito.when(orderRefundPoMapper.selectByExample(Mockito.any())).thenReturn(pos);
    }

    private void mockPaymentDaoFindRefund()
    {
        RefundCheckDto refundCheckDto = RefundCheckDto.builder()
                .id(1L).status(RefundCheckDto.SUCCESS).amount(100L)
                .build();
        InternalReturnObject<RefundCheckDto> returnObject = new InternalReturnObject<>();
        returnObject.setData(refundCheckDto);
        Mockito.when(paymentDao.findRefund(Mockito.any(),Mockito.any())).thenReturn(returnObject);
    }

    private void mockGoodsDaoGetOnsaleById()
    {
        OnsaleDto onsaleDto = OnsaleDto.builder()
                .id(1L).price(12L).maxQuantity(12).type((byte)0)
                .product(IdNameDto.builder().id(1L).name("西瓜霜含片").build())
                .build();
        InternalReturnObject<OnsaleDto> returnObject  = new InternalReturnObject<>();
        returnObject.setData(onsaleDto);
        Mockito.when(goodsDao.getOnsaleById(Mockito.any(),Mockito.any())).thenReturn(returnObject);
    }

    private void mockRetrieveOrderPaymentsByOrderId()
    {
        List<OrderPaymentPo> orderPaymentList = List.of(
                OrderPaymentPo.builder().orderId(1L).paymentId(1L).build()
        );
        Mockito.when(orderPaymentPoMapper.selectByExample(Mockito.any())).thenReturn(orderPaymentList);
    }

    private void mockGoodsDaoGetProductByIdAndShopId()
    {
        InternalReturnObject<ProductDto> returnObject = new InternalReturnObject<>();
        ProductDto productDto = ProductDto.builder()
                .id(1L).name("西瓜霜含片")
                .commissionRatio(80)
                .build();
        returnObject.setData(productDto);
        Mockito.when(goodsDao.getProductByIdAndShopId(Mockito.any(),Mockito.any())).thenReturn(returnObject);
    }

    /**
     * 店家查询名下订单 (概要)
     */
    @Test
    public void retrieveOrder() throws Exception
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        mockFindPackage();
        mockRetrieveOrder();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/12/orders")
                        .header("authorization", adminToken)
                        .param("orderSn","1")
                        .param("customerId","1")
                        .param("page","1")
                        .param("pageSize","10")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                //判断状态
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", CoreMatchers.is("成功")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id",CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize",CoreMatchers.is(1)))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 店家询订单完整信息（普通，团购，预售）。
     * <p>0 普通，1团购， 2预售</p>
     */
    @Test
    void findOrderById() throws Exception
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        mockFindPackage();
        mockFindShop();
        mockFindOrderById(Order.PAID);
        mockFindOrderRefund();
        mockFindCustomer();
        mockPaymentDaoFindRefund();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/1/orders/1")
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                //判断状态
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", CoreMatchers.is("成功")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.orderSn",CoreMatchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status",CoreMatchers.is(Order.PAID)))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 店家修改订单 (留言)
     */
    @Test
    void updateOrderById() throws Exception
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        mockFindOrderById(Order.BALANCE_UNPAID);
        mockFindPackage();
        mockFindShop();
        mockSaveOrder();
        ShopOrderVo vo = ShopOrderVo.builder().message("test").build();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/1/orders/1")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                //判断状态
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", CoreMatchers.is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 商家取消店铺下订单。
     * 需要登录
     * 商铺管理员调用本 API，只能取消店铺的订单
     * 发货前取消
     */
    @Test
    void deleteOrderById() throws Exception
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        mockFindOrderById(Order.BALANCE_UNPAID);
        mockFindPackage();
        mockFindShop();
        mockFindCustomer();
        mockSaveOrder();
        mockGoodsDaoGetOnsaleById();
        mockRetrieveOrderPaymentsByOrderId();
        mockGoodsDaoGetProductByIdAndShopId();

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/1/orders/1")
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                //判断状态
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", CoreMatchers.is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 店家确认订单
     * <p>用此 API 将一个状态为待发货的订单改为待收货，并记录运单信息</p>
     */
    @Test
    void confirmOrderById() throws Exception
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        mockFindOrderById(Order.NOT_SENT);
        mockFindPackage();
        mockFindShop();
        mockFindCustomer();
        mockCreatePackage();
        mockSaveOrder();

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/1/orders/1/confirm")
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                //判断状态
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", CoreMatchers.is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }


}

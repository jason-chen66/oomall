package cn.edu.xmu.oomall.freight.Controller;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.FreightTestApplication;
import cn.edu.xmu.oomall.freight.controller.vo.ExpressVo;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.Region;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.SimpleRegion;
import cn.edu.xmu.oomall.freight.service.openfeign.JtExpressService;
import cn.edu.xmu.oomall.freight.service.openfeign.JtParam.JtCreateExpressRetObj;
import cn.edu.xmu.oomall.freight.service.openfeign.SfExpressService;
import cn.edu.xmu.oomall.freight.service.openfeign.SfParam.SfCreateExpressRetObj;
import cn.edu.xmu.oomall.freight.service.openfeign.ZtoExpressService;
import cn.edu.xmu.oomall.freight.service.openfeign.ZtoParam.ZtoCreateExpressRetObj;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = FreightTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class InternalFreightController1Test {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegionDao regionDao;

    @MockBean
    RedisUtil redisUtil;

    @MockBean
    SfExpressService sfExpressService;

    @MockBean
    ZtoExpressService ztoExpressService;

    @MockBean
    JtExpressService jtExpressService;


    private static String adminToken;

    @BeforeAll
    public static void setup()
    {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    /**************************************************************************
     * 这边的 expressMapper 不是 mockbean
     *************************************************************************/

    private void mockGetRegion()
    {
        InternalReturnObject<Region> o1 = new InternalReturnObject<>();
        o1.setErrno(ReturnNo.OK.getErrNo());
        o1.setErrmsg(ReturnNo.OK.getMessage());
        Region r1 = new Region();
        r1.setId(1L);
        r1.setName("翔安区");
        o1.setData(r1);
        Mockito.when(regionDao.getRegionById(1L)).thenReturn(o1);

        InternalReturnObject<Region> o4 = new InternalReturnObject<>();
        o4.setErrno(ReturnNo.OK.getErrNo());
        o4.setErrmsg(ReturnNo.OK.getMessage());
        Region r4 = new Region();
        r4.setId(2L);
        r4.setName("蓬莱区");
        o4.setData(r4);
        Mockito.when(regionDao.getRegionById(2L)).thenReturn(o4);
    }

    private void mockGetParentRegionList()
    {
        InternalReturnObject<List<SimpleRegion>> simpleRegions = new InternalReturnObject<>();
        List<SimpleRegion> list = List.of(
                SimpleRegion.builder().id(10L).name("厦门市").build(),
                SimpleRegion.builder().id(100L).name("福建省").build()
        );
        simpleRegions.setData(list);
        Mockito.when(regionDao.getParentRegionsById(1L)).thenReturn(simpleRegions);

        List<SimpleRegion> list1 = List.of(
                SimpleRegion.builder().id(20L).name("南天门市").build(),
                SimpleRegion.builder().id(200L).name("天庭省").build()
        );
        simpleRegions.setData(list1);
        Mockito.when(regionDao.getParentRegionsById(2L)).thenReturn(simpleRegions);

    }

     /**
     * 创建运单(顺丰)
     * @throws Exception
     */
    @Test
    public void createPackage1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        mockGetRegion();
        mockGetParentRegionList();

        SfCreateExpressRetObj sfCreateExpressRetObj = new SfCreateExpressRetObj();
        sfCreateExpressRetObj.setErrorCode("S0000");
        sfCreateExpressRetObj.setSuccess("true");
        sfCreateExpressRetObj.setErrorMsg("成功");
        SfCreateExpressRetObj.Result result = new SfCreateExpressRetObj.Result();
        result.setOrderId("QIAO-20200528-006");
        result.setWaybillNoInfoList(new ArrayList<>());
        result.getWaybillNoInfoList().add(new SfCreateExpressRetObj.Result.WaybillNoInfoList(
                (byte) 1,
                "QIAO-20200528-006"
        ));
        sfCreateExpressRetObj.setMsgData(result);
        InternalReturnObject internalReturnObject = new InternalReturnObject();
        internalReturnObject.setData(sfCreateExpressRetObj);
        Mockito.when(sfExpressService.createExpress(Mockito.any())).thenReturn(internalReturnObject);

        ExpressVo vo = new ExpressVo();
        vo.setShopLogisticId(1L);
        ExpressVo.Consignee sender = vo.new Consignee();
        ExpressVo.Consignee delivery = vo.new Consignee();
        sender.setName("李大柱");
        sender.setMobile("12345678910");
        sender.setRegionId(1L);
        sender.setAddress("桂花街桂花路");

        delivery.setName("王二蛋");
        delivery.setMobile("23456789101");
        delivery.setRegionId(2L);
        delivery.setAddress("玫瑰街玫瑰路");
        vo.setSender(sender);
        vo.setDelivery(delivery);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/shops/1/packages")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.data.billCode",is("QIAO-20200528-006")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("创建成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 中通
     * @throws Exception
     */
    @Test
    public void createPackage2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        mockGetRegion();
        mockGetParentRegionList();

        InternalReturnObject<Region> o6 = new InternalReturnObject<>();
        o6.setErrno(ReturnNo.OK.getErrNo());
        o6.setErrmsg(ReturnNo.OK.getMessage());
        Region r6 = new Region();
        r6.setId(200L);
        r6.setName("天庭省");
        o6.setData(r6);
        Mockito.when(regionDao.getRegionById(200L)).thenReturn(o6);

        ZtoCreateExpressRetObj ztoCreateExpressRetObj = new ZtoCreateExpressRetObj();
        ztoCreateExpressRetObj.setStatus(true);
        ztoCreateExpressRetObj.setResult(new ZtoCreateExpressRetObj.Result(new ZtoCreateExpressRetObj.Result.SignBillInfo(
                "QIAO-20200528-006","QIAO-20200528-006","QIAO-20200528-006"
        )));
        InternalReturnObject internalReturnObject = new InternalReturnObject();
        internalReturnObject.setData(ztoCreateExpressRetObj);
        Mockito.when(ztoExpressService.createExpress(Mockito.any())).thenReturn(internalReturnObject);

        ExpressVo vo = new ExpressVo();
        vo.setShopLogisticId(2L);
        ExpressVo.Consignee sender = vo.new Consignee();
        ExpressVo.Consignee delivery = vo.new Consignee();
        sender.setName("李大柱");
        sender.setMobile("12345678910");
        sender.setRegionId(1L);
        sender.setAddress("桂花街桂花路");

        delivery.setName("王二蛋");
        delivery.setMobile("23456789101");
        delivery.setRegionId(2L);
        delivery.setAddress("玫瑰街玫瑰路");
        vo.setSender(sender);
        vo.setDelivery(delivery);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/shops/1/packages")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.data.billCode",is("QIAO-20200528-006")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("创建成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 极兔
     * @throws Exception
     */
    @Test
    public void createPackage3() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        mockGetRegion();
        mockGetParentRegionList();

        JtCreateExpressRetObj jtCreateExpressRetObj = new JtCreateExpressRetObj();
        jtCreateExpressRetObj.setCode("1");
        jtCreateExpressRetObj.setData(new JtCreateExpressRetObj.Result(
                "中国","QIAO-20200528-006","2022-12-30 12:10:01",
                "QIAO-20200528-006","QIAO"
        ));
        InternalReturnObject internalReturnObject = new InternalReturnObject();
        internalReturnObject.setData(jtCreateExpressRetObj);
        Mockito.when(jtExpressService.createExpress(Mockito.any())).thenReturn(internalReturnObject);

        ExpressVo vo = new ExpressVo();
        vo.setShopLogisticId(3L);
        ExpressVo.Consignee sender = vo.new Consignee();
        ExpressVo.Consignee delivery = vo.new Consignee();
        sender.setName("李大柱");
        sender.setMobile("12345678910");
        sender.setRegionId(1L);
        sender.setAddress("桂花街桂花路");

        delivery.setName("王二蛋");
        delivery.setMobile("23456789101");
        delivery.setRegionId(2L);
        delivery.setAddress("玫瑰街玫瑰路");
        vo.setSender(sender);
        vo.setDelivery(delivery);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/shops/1/packages")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.data.billCode",is("QIAO-20200528-006")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("创建成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 添加责任链
     * @throws Exception
     */
    @Test
    public void createPackage0() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        mockGetRegion();
        mockGetParentRegionList();

        SfCreateExpressRetObj sfCreateExpressRetObj = new SfCreateExpressRetObj();
        sfCreateExpressRetObj.setErrorCode("S0000");
        sfCreateExpressRetObj.setSuccess("true");
        sfCreateExpressRetObj.setErrorMsg("成功");
        SfCreateExpressRetObj.Result result = new SfCreateExpressRetObj.Result();
        result.setOrderId("QIAO-20200528-006");
        result.setWaybillNoInfoList(new ArrayList<>());
        result.getWaybillNoInfoList().add(new SfCreateExpressRetObj.Result.WaybillNoInfoList(
                (byte) 1,
                "QIAO-20200528-006"
        ));
        sfCreateExpressRetObj.setMsgData(result);
        InternalReturnObject internalReturnObject = new InternalReturnObject();
        internalReturnObject.setData(sfCreateExpressRetObj);
        Mockito.when(sfExpressService.createExpress(Mockito.any())).thenReturn(internalReturnObject);

        ExpressVo vo = new ExpressVo();
        vo.setShopLogisticId(0L);
        ExpressVo.Consignee sender = vo.new Consignee();
        ExpressVo.Consignee delivery = vo.new Consignee();
        sender.setName("李大柱");
        sender.setMobile("12345678910");
        sender.setRegionId(2L);
        sender.setAddress("桂花街桂花路");

        delivery.setName("王二蛋");
        delivery.setMobile("23456789101");
        delivery.setRegionId(1L);
        delivery.setAddress("玫瑰街玫瑰路");
        vo.setSender(sender);
        vo.setDelivery(delivery);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/shops/1/packages")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.data.billCode",is("QIAO-20200528-006")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("创建成功")))
                .andDo(MockMvcResultHandlers.print());
    }

}

package cn.edu.xmu.oomall.freight.Controller;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.FreightTestApplication;
import cn.edu.xmu.oomall.freight.controller.vo.ConfirmExpressVo;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.Region;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.mapper.generator.ExpressPoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.po.ExpressPo;
import cn.edu.xmu.oomall.freight.service.openfeign.JtExpressService;
import cn.edu.xmu.oomall.freight.service.openfeign.JtParam.JtCancelExpressRetObj;
import cn.edu.xmu.oomall.freight.service.openfeign.JtParam.JtGetExpressRetObj;
import cn.edu.xmu.oomall.freight.service.openfeign.JtParam.JtGetRouteRetObj;
import cn.edu.xmu.oomall.freight.service.openfeign.JtParam.JtRouteResultParam;
import cn.edu.xmu.oomall.freight.service.openfeign.SfExpressService;
import cn.edu.xmu.oomall.freight.service.openfeign.SfParam.SfCancelExpressRetObj;
import cn.edu.xmu.oomall.freight.service.openfeign.SfParam.SfGetExpressRetObj;
import cn.edu.xmu.oomall.freight.service.openfeign.SfParam.SfGetRouteRetObj;
import cn.edu.xmu.oomall.freight.service.openfeign.SfParam.SfRouteResultParam;
import cn.edu.xmu.oomall.freight.service.openfeign.ZtoExpressService;
import cn.edu.xmu.oomall.freight.service.openfeign.ZtoParam.ZtoCancelExpressRetObj;
import cn.edu.xmu.oomall.freight.service.openfeign.ZtoParam.ZtoGetExpressRetObj;
import cn.edu.xmu.oomall.freight.service.openfeign.ZtoParam.ZtoGetRouteRetObj;
import cn.edu.xmu.oomall.freight.service.openfeign.ZtoParam.ZtoRouteResultParam;
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

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = FreightTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class InternalFreightControllerTest {
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

    @MockBean
    ExpressPoMapper expressPoMapper;

    private static String adminToken;

    @BeforeAll
    public static void setup()
    {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }


    /**
     * 顺丰
     * */
    @Test
    public void getPackage1() throws Exception
    {
        ExpressPo expressPo = ExpressPo.builder()
                .id(1L)
                .billCode("111L")
                .shopId(1L)
                .shopLogisticsId(1L)
                .senderName("李大柱")
                .senderMobile("12345678910")
                .senderRegionId(1L)
                .senderAddress("桂花街桂花路")
                .deliverName("王二蛋")
                .deliverMobile("23456789101")
                .deliverRegionId(2L)
                .deliverAddress("玫瑰街玫瑰路")
                .status(Express.ON)
                .build();
        List<ExpressPo> expressPos = new ArrayList<>();
        expressPos.add(expressPo);
        Mockito.when(expressPoMapper.selectByExample(Mockito.any())).thenReturn(expressPos);

        Mockito.when(expressPoMapper.updateByExampleSelective(Mockito.any(),Mockito.any())).thenReturn(1);

        SfGetRouteRetObj sfGetRouteRetObj = new SfGetRouteRetObj();
        sfGetRouteRetObj.setErrorCode("S0000");
        sfGetRouteRetObj.setSuccess("true");
        sfGetRouteRetObj.setErrorMsg("成功");
        SfGetRouteRetObj.Result result = new SfGetRouteRetObj.Result();
        List<SfRouteResultParam> sfRouteResultParams = new ArrayList<>();
        SfRouteResultParam sfRouteResultParam1 = new SfRouteResultParam();
        sfRouteResultParam1.setMailNo("SF1011603494291");

        sfRouteResultParam1.setRoutes(new ArrayList<>());

        sfRouteResultParam1.getRoutes().add(new SfRouteResultParam.Route(
                "2019-05-09 10:11:26",
                "深圳",
                "已派件",
                "50"
        ));
        sfRouteResultParam1.getRoutes().add(new SfRouteResultParam.Route(
                "2019-05-09 18:11:26",
                "深圳",
                "已签收",
                "80"
        ));
        sfRouteResultParams.add(sfRouteResultParam1);
        result.setRouteResps(sfRouteResultParams);
        sfGetRouteRetObj.setMsgData(result);
        InternalReturnObject i1 = new InternalReturnObject();
        i1.setData(sfGetRouteRetObj);
        Mockito.when(sfExpressService.getRoute(Mockito.any())).thenReturn(i1);

        SfGetExpressRetObj sfGetExpressRetObj = new SfGetExpressRetObj();
        sfGetExpressRetObj.setErrorCode("S0000");
        sfGetExpressRetObj.setSuccess("true");
        sfGetExpressRetObj.setErrorMsg("成功");
        InternalReturnObject i2 = new InternalReturnObject();
        i2.setData(sfGetExpressRetObj);
        Mockito.when(sfExpressService.getExpressByOrderId(Mockito.any())).thenReturn(i2);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/internal/shops/1/packages")
                    .header("authorization", adminToken)
                    .param("billCode","111L")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                //已签收，状态为2
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status",is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.billCode",is("111L")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 中通
     */

    @Test
    public void getPackage2() throws Exception
    {
        ExpressPo expressPo = ExpressPo.builder()
                .id(1L)
                .billCode("111L")
                .shopId(1L)
                .shopLogisticsId(2L)
                .senderName("李大柱")
                .senderMobile("12345678910")
                .senderRegionId(1L)
                .senderAddress("桂花街桂花路")
                .deliverName("王二蛋")
                .deliverMobile("23456789101")
                .deliverRegionId(2L)
                .deliverAddress("玫瑰街玫瑰路")
                .status(Express.ON)
                .build();
        List<ExpressPo> expressPos = new ArrayList<>();
        expressPos.add(expressPo);
        Mockito.when(expressPoMapper.selectByExample(Mockito.any())).thenReturn(expressPos);

        ZtoGetExpressRetObj ztoGetExpressRetObj = new ZtoGetExpressRetObj();
        ztoGetExpressRetObj.setMessage("字符串");
        ztoGetExpressRetObj.setStatusCode("0000");
        ztoGetExpressRetObj.setStatus(true);
        ZtoGetExpressRetObj.PreOrderInfoDto preOrderInfoDto = new ZtoGetExpressRetObj.PreOrderInfoDto();
        //物流状态 99-已签收
        preOrderInfoDto.setOrderStatus(99);
        preOrderInfoDto.setBillCode("12123412434");
        preOrderInfoDto.setOrderCode("130005102254");
        List<ZtoGetExpressRetObj.PreOrderInfoDto> result = List.of(preOrderInfoDto);
        ztoGetExpressRetObj.setData(result);
        InternalReturnObject i = new InternalReturnObject();
        i.setData(ztoGetExpressRetObj);
        Mockito.when(ztoExpressService.getExpressByOrderId(Mockito.any())).thenReturn(i);

        ZtoGetRouteRetObj ztoGetRouteRetObj = new ZtoGetRouteRetObj();
        ztoGetRouteRetObj.setMessage("字符串");
        ztoGetRouteRetObj.setStatusCode("0000");
        ztoGetRouteRetObj.setStatus(true);
        ZtoRouteResultParam ztoRouteResultParam = new ZtoRouteResultParam();
        ztoRouteResultParam.setCountry("China");
        ztoRouteResultParam.setSignMan("李四");
        ztoRouteResultParam.setOperateUser("王五");
        ztoRouteResultParam.setOperateUserPhone("18866666666");
        ztoRouteResultParam.setBillCode("73111390619708");
        ztoRouteResultParam.setScanType("收件");
        ztoRouteResultParam.setBillCode("12123412434");
        ztoRouteResultParam.setScanDate("2020-12-12 12:12:12");
        ZtoRouteResultParam.PreOrNextSite preOrNextSite = new ZtoRouteResultParam.PreOrNextSite();
        preOrNextSite.setIsTransfer(1);
        preOrNextSite.setName("廊坊");
        preOrNextSite.setPhone("0316-7705555");
        preOrNextSite.setProv("河北省");
        ZtoRouteResultParam.ScanSite scanSite = new ZtoRouteResultParam.ScanSite();
        scanSite.setPhone("0316-7705555");
        scanSite.setIsTransfer(1);
        scanSite.setName("廊坊");
        scanSite.setProv("河北省");
        ztoRouteResultParam.setScanSite(scanSite);
        ztoRouteResultParam.setPreSite(preOrNextSite);
        ztoRouteResultParam.setDesc("【上海】（021-605511111） 的小吉（18888888888） 已揽收");
        List<ZtoRouteResultParam> result1 = List.of(ztoRouteResultParam);
        ztoGetRouteRetObj.setResult(result1);
        InternalReturnObject i1 = new InternalReturnObject();
        i1.setData(ztoGetRouteRetObj);
        Mockito.when(ztoExpressService.getRoute(Mockito.any())).thenReturn(i1);

        Mockito.when(expressPoMapper.updateByExampleSelective(Mockito.any(),Mockito.any())).thenReturn(1);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/internal/shops/1/packages")
                        .header("authorization", adminToken)
                        .param("billCode","111L")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.billCode",is("111L")))
                //已签收
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status",is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getPackage3() throws Exception
    {
        ExpressPo expressPo = ExpressPo.builder()
                .id(1L)
                .billCode("111L")
                .shopId(1L)
                .shopLogisticsId(3L)
                .senderName("李大柱")
                .senderMobile("12345678910")
                .senderRegionId(1L)
                .senderAddress("桂花街桂花路")
                .deliverName("王二蛋")
                .deliverMobile("23456789101")
                .deliverRegionId(2L)
                .deliverAddress("玫瑰街玫瑰路")
                .status(Express.ON)
                .build();
        List<ExpressPo> expressPos = new ArrayList<>();
        expressPos.add(expressPo);
        Mockito.when(expressPoMapper.selectByExample(Mockito.any())).thenReturn(expressPos);

        JtGetExpressRetObj jtGetExpressRetObj = new JtGetExpressRetObj();
        jtGetExpressRetObj.setCode("1");
        jtGetExpressRetObj.setMsg("success");
        jtGetExpressRetObj.setData(new ArrayList<>());
        JtGetExpressRetObj.Result result = new JtGetExpressRetObj.Result();

        result.setLastCenterName("深圳转运中心");
        //运单状态 103-已签收
        result.setOrderStatus("103");

        result.setOrderNumber(26666027030937624L);
        result.setTxlogisticId("1598516207047");
        result.setBillCode("YL1234567890123");
        result.setOrderType("1");
        result.setSortingCode("110 110-120 130");
        result.setRemark("重要文件");
        result.setWeight("1");

        JtGetExpressRetObj.Result.Sender sender = new JtGetExpressRetObj.Result.Sender();
        sender.setName("李白");
        sender.setMobile("12345678909");
        sender.setPhone("");
        sender.setCountryCode(null);
        sender.setProv("广东省");
        sender.setCity("深圳");
        sender.setArea("南山区");
        sender.setAddress("粤美特大厦");
        result.setSender(sender);

        JtGetExpressRetObj.Result.Receiver receiver = new JtGetExpressRetObj.Result.Receiver();
        receiver.setName("高渐离");
        receiver.setMobile("12345678909");
        receiver.setPhone("");
        receiver.setCountryCode(null);
        receiver.setProv("广东省");
        receiver.setCity("深圳");
        receiver.setArea("南山区");
        receiver.setAddress("粤美特大厦");
        result.setReceiver(receiver);

        jtGetExpressRetObj.getData().add(result);
        InternalReturnObject i = new InternalReturnObject();
        i.setData(jtGetExpressRetObj);
        Mockito.when(jtExpressService.getExpressByOrderId(Mockito.any())).thenReturn(i);

        JtGetRouteRetObj jtGetRouteRetObj = JtGetRouteRetObj.builder()
                .code("1")
                .msg("success")
                .data(new JtGetRouteRetObj.Result())
                .build();
        jtGetRouteRetObj.getData().setBillCode("UT0000352320970");
        jtGetRouteRetObj.getData().setDetails(new ArrayList<>());
        jtGetRouteRetObj.getData().getDetails().add(
                JtRouteResultParam.builder()
                        .scanTime("2020-07-18 08:53:05")
                        .desc("包裹已签收！签收人是【本人签收】，如有疑问请联系：13123456789，如需联系网点请拨打：17314954950 特 殊时期，极兔从不懈怠，感谢使用，我们时刻准备，再次为您服务！")
                        .scanType("快件签收")
                        .scanNetworkName("南京玄武网点")
                        .scanNetworkId(String.valueOf(33))
                        .staffName("test1042")
                        .staffContact("13123456789")
                        .scanNetworkProvince("江苏省")
                        .scanNetworkCity("南京市")
                        .scanNetworkArea("玄武区")
                        .build()
        );

        jtGetRouteRetObj.getData().getDetails().add(
                JtRouteResultParam.builder()
                        .scanTime("2020-07-18 08:52:53")
                        .desc("快件离开【南京转运中心】已发往【南京玄武网点】，您的极兔包，离目的地更近一步啦！")
                        .scanType("发件扫描")
                        .scanNetworkName("南京转运中心")
                        .scanNetworkId(String.valueOf(24))
                        .scanNetworkProvince("江苏省")
                        .scanNetworkCity("南京市")
                        .scanNetworkArea("江宁区")
                        .nextStopName("南京玄武网点")
                        .build()
        );
        InternalReturnObject i1 = new InternalReturnObject();
        i1.setData(jtGetRouteRetObj);
        Mockito.when(jtExpressService.getRoute(Mockito.any())).thenReturn(i1);

        Mockito.when(expressPoMapper.updateByExampleSelective(Mockito.any(),Mockito.any())).thenReturn(1);

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

        this.mockMvc.perform(MockMvcRequestBuilders.get("/internal/shops/1/packages")
                        .header("authorization", adminToken)
                        .param("billCode","111L")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.billCode",is("111L")))
                //已签收
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status",is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test //顺丰
    public void getPackage10() throws Exception{
        ExpressPo expressPo = ExpressPo.builder()
                .id(1L)
                .billCode("111L")
                .shopId(1L)
                .shopLogisticsId(1L)
                .senderName("李大柱")
                .senderMobile("12345678910")
                .senderRegionId(1L)
                .senderAddress("桂花街桂花路")
                .deliverName("王二蛋")
                .deliverMobile("23456789101")
                .deliverRegionId(2L)
                .deliverAddress("玫瑰街玫瑰路")
                .status(Express.ON)
                .build();
        Mockito.when(expressPoMapper.selectByPrimaryKey(Mockito.any())).thenReturn(expressPo);

        Mockito.when(expressPoMapper.updateByExampleSelective(Mockito.any(),Mockito.any())).thenReturn(1);

        SfGetRouteRetObj sfGetRouteRetObj = new SfGetRouteRetObj();
        sfGetRouteRetObj.setErrorCode("S0000");
        sfGetRouteRetObj.setSuccess("true");
        sfGetRouteRetObj.setErrorMsg("成功");
        SfGetRouteRetObj.Result result = new SfGetRouteRetObj.Result();
        List<SfRouteResultParam> sfRouteResultParams = new ArrayList<>();
        SfRouteResultParam sfRouteResultParam1 = new SfRouteResultParam();
        sfRouteResultParam1.setMailNo("SF1011603494291");

        sfRouteResultParam1.setRoutes(new ArrayList<>());
        //运单信息由路由判断,重要字段:路由信息
        sfRouteResultParam1.getRoutes().add(new SfRouteResultParam.Route(
                "2019-05-09 10:11:26",
                "深圳",
                "已派件",
                "50"
        ));
        sfRouteResultParam1.getRoutes().add(new SfRouteResultParam.Route(
                "2019-05-09 18:11:26",
                "深圳",
                "已签收",
                "80"
        ));
        sfRouteResultParams.add(sfRouteResultParam1);
        result.setRouteResps(sfRouteResultParams);
        sfGetRouteRetObj.setMsgData(result);
        InternalReturnObject i = new InternalReturnObject();
        i.setData(sfGetRouteRetObj);
        Mockito.when(sfExpressService.getRoute(Mockito.any())).thenReturn(i);

        SfGetExpressRetObj sfGetExpressRetObj = new SfGetExpressRetObj();
        sfGetExpressRetObj.setErrorCode("S0000");
        sfGetExpressRetObj.setSuccess("true");
        sfGetExpressRetObj.setErrorMsg("成功");
        InternalReturnObject i1 = new InternalReturnObject();
        i1.setData(sfGetExpressRetObj);
        Mockito.when(sfExpressService.getExpressByOrderId(Mockito.any())).thenReturn(i1);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/internal/packages/222")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.billCode",is("111L")))
                //已签收
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status",is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 测试顺丰
     * @throws Exception
     */
    @Test
    public void cancalPackage1() throws Exception{
        ExpressPo expressPo = ExpressPo.builder()
                .id(1L)
                .billCode("111L")
                .shopId(1L)
                .shopLogisticsId(1L)
                .senderName("李大柱")
                .senderMobile("12345678910")
                .senderRegionId(1L)
                .senderAddress("桂花街桂花路")
                .deliverName("王二蛋")
                .deliverMobile("23456789101")
                .deliverRegionId(2L)
                .deliverAddress("玫瑰街玫瑰路")
                .status(Express.UNSENT)
                .build();
        List<ExpressPo> expressPos = new ArrayList<>();
        expressPos.add(expressPo);
        Mockito.when(expressPoMapper.selectByExample(Mockito.any())).thenReturn(expressPos);

        Mockito.when(expressPoMapper.selectByPrimaryKey(Mockito.any())).thenReturn(expressPo);

        Mockito.when(expressPoMapper.updateByExampleSelective(Mockito.any(),Mockito.any())).thenReturn(1);

        SfCancelExpressRetObj retObj = new SfCancelExpressRetObj();
        retObj.setSuccess("true");
        retObj.setErrorCode("S0000");
        retObj.setErrorMsg("成功");
        InternalReturnObject i = new InternalReturnObject();
        i.setData(retObj);
        Mockito.when(sfExpressService.cancelExpress(Mockito.any())).thenReturn(i);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/internal/shops/1/packages/222/cancel")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 测试极兔
     * @throws Exception
     */
    @Test
    public void cancelPackage2() throws Exception{
        ExpressPo expressPo = ExpressPo.builder()
                .id(1L)
                .billCode("111L")
                .shopId(1L)
                .shopLogisticsId(3L)
                .senderName("李大柱")
                .senderMobile("12345678910")
                .senderRegionId(1L)
                .senderAddress("桂花街桂花路")
                .deliverName("王二蛋")
                .deliverMobile("23456789101")
                .deliverRegionId(2L)
                .deliverAddress("玫瑰街玫瑰路")
                .status(Express.UNSENT)
                .build();
        List<ExpressPo> expressPos = new ArrayList<>();
        expressPos.add(expressPo);
        Mockito.when(expressPoMapper.selectByExample(Mockito.any())).thenReturn(expressPos);

        Mockito.when(expressPoMapper.selectByPrimaryKey(Mockito.any())).thenReturn(expressPo);

        Mockito.when(expressPoMapper.updateByExampleSelective(Mockito.any(),Mockito.any())).thenReturn(1);

        JtCancelExpressRetObj jtCancelExpressRetObj = new JtCancelExpressRetObj();
        jtCancelExpressRetObj.setCode("1");
        jtCancelExpressRetObj.setMsg("success");
        InternalReturnObject i = new InternalReturnObject();
        i.setData(jtCancelExpressRetObj);
        Mockito.when(jtExpressService.cancelExpress(Mockito.any())).thenReturn(i);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/internal/shops/1/packages/222/cancel")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 测试中通
     * @throws Exception
     */
    @Test
    public void cancelPackage3() throws Exception{
        ExpressPo expressPo = ExpressPo.builder()
                .id(1L)
                .billCode("111L")
                .shopId(1L)
                .shopLogisticsId(2L)
                .senderName("李大柱")
                .senderMobile("12345678910")
                .senderRegionId(1L)
                .senderAddress("桂花街桂花路")
                .deliverName("王二蛋")
                .deliverMobile("23456789101")
                .deliverRegionId(2L)
                .deliverAddress("玫瑰街玫瑰路")
                .status(Express.UNSENT)
                .build();
        List<ExpressPo> expressPos = new ArrayList<>();
        expressPos.add(expressPo);
        Mockito.when(expressPoMapper.selectByExample(Mockito.any())).thenReturn(expressPos);

        Mockito.when(expressPoMapper.selectByPrimaryKey(Mockito.any())).thenReturn(expressPo);

        Mockito.when(expressPoMapper.updateByExampleSelective(Mockito.any(),Mockito.any())).thenReturn(1);

        ZtoCancelExpressRetObj ztoCancelExpressRetObj = new ZtoCancelExpressRetObj();
        ztoCancelExpressRetObj.setStatus(true);
        ztoCancelExpressRetObj.setMessage("success");
        InternalReturnObject i = new InternalReturnObject();
        i.setData(ztoCancelExpressRetObj);
        Mockito.when(ztoExpressService.cancelExpress(Mockito.any())).thenReturn(i);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/internal/shops/1/packages/222/cancel")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void confirmPackage1() throws Exception{
        ExpressPo expressPo = ExpressPo.builder()
                .id(1L)
                .billCode("111L")
                .shopId(1L)
                .shopLogisticsId(1L)
                .senderName("李大柱")
                .senderMobile("12345678910")
                .senderRegionId(1L)
                .senderAddress("桂花街桂花路")
                .deliverName("王二蛋")
                .deliverMobile("23456789101")
                .deliverRegionId(2L)
                .deliverAddress("玫瑰街玫瑰路")
                .status(Express.RETURNED)
                .build();
        Mockito.when(expressPoMapper.selectByPrimaryKey(Mockito.any())).thenReturn(expressPo);

        Mockito.when(expressPoMapper.updateByExampleSelective(Mockito.any(),Mockito.any())).thenReturn(1);

        ConfirmExpressVo vo = new ConfirmExpressVo();
        vo.setStatus(0);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/internal/shop/1/packages/222/confirm")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JacksonUtil.toJson(vo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void confirmPackage2() throws Exception{
        ExpressPo expressPo = ExpressPo.builder()
                .id(1L)
                .billCode("111L")
                .shopId(1L)
                .shopLogisticsId(1L)
                .senderName("李大柱")
                .senderMobile("12345678910")
                .senderRegionId(1L)
                .senderAddress("桂花街桂花路")
                .deliverName("王二蛋")
                .deliverMobile("23456789101")
                .deliverRegionId(2L)
                .deliverAddress("玫瑰街玫瑰路")
                .status(Express.RETURNED)
                .build();
        Mockito.when(expressPoMapper.selectByPrimaryKey(Mockito.any())).thenReturn(expressPo);

        Mockito.when(expressPoMapper.updateByExampleSelective(Mockito.any(),Mockito.any())).thenReturn(1);

        ConfirmExpressVo vo = new ConfirmExpressVo();
        vo.setStatus(1);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/internal/shop/1/packages/222/confirm")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JacksonUtil.toJson(vo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

}

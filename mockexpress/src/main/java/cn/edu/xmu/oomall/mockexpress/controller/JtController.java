package cn.edu.xmu.oomall.mockexpress.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.mockexpress.controller.vo.JtParam.*;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RefreshScope
@RequestMapping(value = "/jtexpress", produces = "application/json;charset=UTF-8")
public class JtController {
    @PostMapping("/webopenplatformapi/api/order/addOrder")
    public ReturnObject addOrder(@RequestBody JtCreateExpressParam jtCreateExpressParam){
        JtCreateExpressRetObj jtCreateExpressRetObj = new JtCreateExpressRetObj();
        jtCreateExpressRetObj.setCode("1");
        jtCreateExpressRetObj.setMsg("success");
        jtCreateExpressRetObj.setData(new JtCreateExpressRetObj.Result());
        jtCreateExpressRetObj.getData().setCreateOrderTime("2022-07-04 12:00:53");
        jtCreateExpressRetObj.getData().setLastCenterName("华东转运中心B1");
        jtCreateExpressRetObj.getData().setSortingCode("382 300-64 010");
        jtCreateExpressRetObj.getData().setBillCode("UT0000498364212");
        jtCreateExpressRetObj.getData().setTxlogisticId("TEST20220704210006");
        return new ReturnObject(ReturnNo.OK,jtCreateExpressRetObj);
    }

    @PostMapping("/webopenplatformapi/api/order/cancelOrder")
    public ReturnObject cancelOrder(@RequestBody JtCancelExpressParam jtCancelExpressParam){
        JtCancelExpressRetObj jtCancelExpressRetObj = new JtCancelExpressRetObj();
        jtCancelExpressRetObj.setCode("1");
        jtCancelExpressRetObj.setMsg("success");
        jtCancelExpressRetObj.setData(new JtCancelExpressRetObj.Result());
        jtCancelExpressRetObj.getData().setBillCode("UT0000272932121");
        jtCancelExpressRetObj.getData().setTxlogisticId("1598516207047");
        return new ReturnObject(ReturnNo.OK,jtCancelExpressRetObj);
    }

    @PostMapping ("webopenplatformapi/api/order/getOrders")
    public ReturnObject getOrders(@RequestBody JtGetExpressParam jtGetExpressParam){
        JtGetExpressRetObj jtGetExpressRetObj = new JtGetExpressRetObj();
        jtGetExpressRetObj.setCode("1");
        jtGetExpressRetObj.setMsg("success");
        jtGetExpressRetObj.setData(new ArrayList<>());
        JtGetExpressRetObj.Result result = new JtGetExpressRetObj.Result();

        result.setLastCenterName("深圳转运中心");

        //重要的字段
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
        return new ReturnObject(ReturnNo.OK,jtGetExpressRetObj);
    }
    @PostMapping("/webopenplatformapi/api/logistics/trace")
    public ReturnObject trace(@RequestBody JtGetRouteParam jtGetRouteParam){
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

        jtGetRouteRetObj.getData().getDetails().add(
                JtRouteResultParam.builder()
                        .scanTime("2020-07-18 08:52:51")
                        .desc("快件到达【南京转运中心】，您的极兔包，离目的地更近一步啦！")
                        .scanType("到件扫描")
                        .scanNetworkName("南京转运中心")
                        .scanNetworkId(String.valueOf(24))
                        .scanNetworkProvince("江苏省")
                        .scanNetworkCity("南京市")
                        .scanNetworkArea("江宁区")
                        .nextStopName("南京转运中心")
                        .build()
        );

        jtGetRouteRetObj.getData().getDetails().add(
                JtRouteResultParam.builder()
                        .scanTime("2020-07-18 08:52:50")
                        .desc("快件离开【南京雨花台春江新城网点】已发往【南京转运中心】，您的极兔包，离目的地更近一步啦！")
                        .scanType("发件扫描")
                        .scanNetworkName("南京雨花台春江新城网点")
                        .scanNetworkId(String.valueOf(1772))
                        .scanNetworkProvince("江苏省")
                        .scanNetworkCity("南京市")
                        .scanNetworkArea("江宁区")
                        .nextStopName("南京转运中心")
                        .build()
        );
        jtGetRouteRetObj.getData().getDetails().add(
                JtRouteResultParam.builder()
                        .scanTime("2020-07-18 08:52:48")
                        .desc("包裹顺利到达【南京雨花台春江新城网点】15850664590，放心交给极兔小哥吧！")
                        .scanType("入仓扫描")
                        .scanNetworkName("南京雨花台春江新城网点")
                        .scanNetworkId(String.valueOf(1772))
                        .scanNetworkProvince("江苏省")
                        .scanNetworkCity("南京市")
                        .scanNetworkArea("江宁区")
                        .build()
        );

        jtGetRouteRetObj.getData().getDetails().add(
                JtRouteResultParam.builder()
                        .scanTime("2020-07-18 08:52:43")
                        .desc("【南京雨花台春江新城网点】您的极兔小哥test1040(13123456789)已取件。如需联系网点，请拨打 15850664590 特殊时期，您的牵挂，让极兔小哥为您速递！ᕱ⑅ᕱ")
                        .scanType("快件揽收")
                        .scanNetworkName("南京雨花台春江新城网点")
                        .scanNetworkId(String.valueOf(1772))
                        .scanNetworkProvince("江苏省")
                        .scanNetworkCity("南京市")
                        .scanNetworkArea("江宁区")
                        .staffName("test1040")
                        .staffContact("13123456789")
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
        jtGetRouteRetObj.getData().getDetails().add(
                JtRouteResultParam.builder()
                        .scanTime("2020-07-18 08:52:51")
                        .desc("快件到达【南京转运中心】，您的极兔包，离目的地更近一步啦！")
                        .scanType("到件扫描")
                        .scanNetworkName("南京转运中心")
                        .scanNetworkId(String.valueOf(24))
                        .scanNetworkProvince("江苏省")
                        .scanNetworkCity("南京市")
                        .scanNetworkArea("江宁区")
                        .build()
        );
        return new ReturnObject(ReturnNo.OK,jtGetRouteRetObj);
    }
}

package cn.edu.xmu.oomall.mockexpress.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.mockexpress.controller.vo.SfParam.*;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RefreshScope
@RequestMapping(value = "/sfexpress", produces = "application/json;charset=UTF-8")
public class SfController {
    @PostMapping("/std/service/EXP_RECE_CREATE_ORDER")
    public ReturnObject createOrder(@RequestBody SfCreateExpressParam sfCreateExpressParam){
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
        return new ReturnObject(ReturnNo.OK,sfCreateExpressRetObj);
    }

    @PostMapping("/std/service/EXP_RECE_SEARCH_ORDER_RESP")
    public ReturnObject getOrders(SfGetExpressParam sfGetExpressParam){
        SfGetExpressRetObj sfGetExpressRetObj = new SfGetExpressRetObj();
        sfGetExpressRetObj.setErrorCode("S0000");
        sfGetExpressRetObj.setSuccess("true");
        sfGetExpressRetObj.setErrorMsg("成功");
        SfGetExpressRetObj.Result result = new SfGetExpressRetObj.Result();
        result.setOrderId("QIAO-20200528-006");
        result.setResStatus((byte) 2);
        result.setWaybillNoInfoList(new ArrayList<>());
        result.getWaybillNoInfoList().add(new SfGetExpressRetObj.Result.WaybillNoInfoList(
                (byte) 1,
                "QIAO-20200528-006"
        ));
        sfGetExpressRetObj.setMsgData(result);
        return new ReturnObject(ReturnNo.OK,sfGetExpressRetObj);
    }

    @PostMapping("/std/service/EXP_RECE_UPDATE_ORDER")
    public ReturnObject cancelOrder(SfCancelExpressParam sfCancelExpressParam){
        SfCancelExpressRetObj sfCancelExpressRetObj = new SfCancelExpressRetObj();
        sfCancelExpressRetObj.setErrorCode("S0000");
        sfCancelExpressRetObj.setSuccess("true");
        sfCancelExpressRetObj.setErrorMsg("成功");
        SfCancelExpressRetObj.Result result = new SfCancelExpressRetObj.Result();
        result.setOrderId("eb21c793-a45a-4d1e-9a2e-1b6e0cd49668");
        result.setResStatus((byte) 2);
        result.setWaybillNoInfoList(new ArrayList<>());
        result.getWaybillNoInfoList().add(new SfCancelExpressRetObj.Result.WaybillNoInfoList(
                (byte) 1,
                "QIAO-20200528-006"
        ));
        sfCancelExpressRetObj.setMsgData(result);
        return new ReturnObject(ReturnNo.OK,sfCancelExpressRetObj);
    }

    @PostMapping("/std/service/EXP_RECE_SEARCH_ROUTES")
    public ReturnObject getRoute(SfGetRouteParam sfGetRouteParam) throws ParseException {
        SfGetRouteRetObj sfGetRouteRetObj = new SfGetRouteRetObj();
        sfGetRouteRetObj.setErrorCode("S0000");
        sfGetRouteRetObj.setSuccess("true");
        sfGetRouteRetObj.setErrorMsg("成功");
        SfGetRouteRetObj.Result result = new SfGetRouteRetObj.Result();
        List<SfRouteResultParam> sfRouteResultParams = new ArrayList<>();
        SfRouteResultParam sfRouteResultParam1 = new SfRouteResultParam();
        sfRouteResultParam1.setMailNo("SF1011603494291");
        sfRouteResultParam1.setRoutes(new ArrayList<>());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        return new ReturnObject(ReturnNo.OK,sfGetRouteRetObj);
    }

}

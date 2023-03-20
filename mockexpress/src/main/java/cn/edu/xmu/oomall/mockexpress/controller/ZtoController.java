package cn.edu.xmu.oomall.mockexpress.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.mockexpress.controller.vo.ZtoParam.*;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RefreshScope
@RequestMapping(value = "/ztoexpress", produces = "application/json;charset=UTF-8")
public class ZtoController {
    @PostMapping("/zto.open.createOrder")
    public ReturnObject addOrder(ZtoCreateExpressParam ztoCreateExpressParam){
        ZtoCreateExpressRetObj ztoCreateExpressRetObj = new ZtoCreateExpressRetObj();
        ztoCreateExpressRetObj.setMessage("字符串");
        ztoCreateExpressRetObj.setStatusCode("0000");
        ztoCreateExpressRetObj.setStatus(true);
        ZtoCreateExpressRetObj.Result.SignBillInfo signBillInfo = new ZtoCreateExpressRetObj.Result.SignBillInfo(
                "12123412434",
                "130005102254",
                "43423424"
        );
        ZtoCreateExpressRetObj.Result result =  new ZtoCreateExpressRetObj.Result();
        result.setSignBillInfo(signBillInfo);
        ztoCreateExpressRetObj.setResult(result);
        return new ReturnObject(ReturnNo.OK,ztoCreateExpressRetObj);
    }

    @PostMapping("/zto.open.getOrderInfo")
    public ReturnObject getOrder(ZtoGetExpressParam ztoGetExpressParam){
        ZtoGetExpressRetObj ztoGetExpressRetObj = new ZtoGetExpressRetObj();
        ztoGetExpressRetObj.setMessage("字符串");
        ztoGetExpressRetObj.setStatusCode("0000");
        ztoGetExpressRetObj.setStatus(true);
        ZtoGetExpressRetObj.PreOrderInfoDto preOrderInfoDto = new ZtoGetExpressRetObj.PreOrderInfoDto();

        //重要的字段,运单状态
        preOrderInfoDto.setOrderStatus(99);

        preOrderInfoDto.setBillCode("12123412434");
        preOrderInfoDto.setOrderCode("130005102254");
        preOrderInfoDto.setSendName("张三");
        preOrderInfoDto.setSendMobile("12345678901");
        preOrderInfoDto.setSendAddress("北京市海淀区");
        preOrderInfoDto.setSendPhone("010-12345678");
        preOrderInfoDto.setSendProv("北京市");
        preOrderInfoDto.setSendCity("北京市");
        preOrderInfoDto.setSendCounty("海淀区");
        preOrderInfoDto.setReceivName("李四");
        preOrderInfoDto.setReceivMobile("12345678901");
        preOrderInfoDto.setReceivAddress("北京市海淀区");
        preOrderInfoDto.setReceivPhone("010-12345678");
        preOrderInfoDto.setReceivProv("北京市");
        preOrderInfoDto.setReceivCity("北京市");
        preOrderInfoDto.setReceivCounty("海淀区");
        preOrderInfoDto.setOrderRemark("备注");
        List<ZtoGetExpressRetObj.PreOrderInfoDto> result = List.of(preOrderInfoDto);
        ztoGetExpressRetObj.setResult(result);
        return new ReturnObject(ReturnNo.OK,ztoGetExpressRetObj);
    }

    @PostMapping("/zto.open.getRouteInfo")
    public ReturnObject getRoute( ZtoGetRouteParam ztoGetRouteParam){
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
        List<ZtoRouteResultParam> result = List.of(ztoRouteResultParam);
        ztoGetRouteRetObj.setResult(result);
        return new ReturnObject(ReturnNo.OK,ztoGetRouteRetObj);
    }

    @PostMapping("/zto.open.cancelPreOrder")
    public ReturnObject cancelOrder(ZtoCancelExpressParam ztoCancelExpressParam){
        ZtoCancelExpressRetObj ztoCancelExpressRetObj = new ZtoCancelExpressRetObj();
        ztoCancelExpressRetObj.setMessage("字符串");
        ztoCancelExpressRetObj.setStatusCode("0000");
        ztoCancelExpressRetObj.setStatus(true);
        return new ReturnObject(ReturnNo.OK,ztoCancelExpressRetObj);
    }
}

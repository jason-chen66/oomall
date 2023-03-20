package cn.edu.xmu.oomall.freight.service.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.freight.service.openfeign.ZtoParam.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mockexpress-service",contextId = "zto")
public interface ZtoExpressService {
    /**
     * 创建运单
     */
    @PostMapping("/ztoexpress/zto.open.createOrder")
    public InternalReturnObject<ZtoCreateExpressRetObj> createExpress(ZtoCreateExpressParam param);


    /**
     * 查询运单(未收到中通返回信息)
     */
    @PostMapping("/ztoexpress/zto.open.getOrderInfo")
    public InternalReturnObject<ZtoGetExpressRetObj> getExpressByOrderId(ZtoGetExpressParam param);

    /**
     * 查询物流
     */
    @PostMapping("/ztoexpress/zto.open.getRouteInfo")
    public InternalReturnObject<ZtoGetRouteRetObj> getRoute(ZtoGetRouteParam param);

    /**
     * 取消运单
     */
    @PostMapping("/ztoexpress/zto.open.cancelPreOrder")
    public InternalReturnObject<ZtoCancelExpressRetObj> cancelExpress(ZtoCancelExpressParam param);
}

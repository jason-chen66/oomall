package cn.edu.xmu.oomall.freight.service.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.freight.service.openfeign.JtParam.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mockexpress-service",contextId = "jt")
public interface JtExpressService {
    /**
     * 创建运单
     * */
    @PostMapping("/jtexpress/webopenplatformapi/api/order/addOrder")
    public InternalReturnObject<JtCreateExpressRetObj> createExpress(JtCreateExpressParam param);

    /**
     * 查询运单(未收到中通返回信息)
     * */
    @PostMapping("/jtexpress/webopenplatformapi/api/order/getOrders")
    public InternalReturnObject<JtGetExpressRetObj> getExpressByOrderId(JtGetExpressParam param);

    /**
     * 查询物流
     * */
    @PostMapping("/jtexpress/webopenplatformapi/api/logistics/trace")
    public InternalReturnObject<JtGetRouteRetObj> getRoute(JtGetRouteParam param);

    /**
     * 取消运单
     * */
    @PostMapping("/jtexpress/webopenplatformapi/api/order/cancelOrder")
    public InternalReturnObject<JtCancelExpressRetObj> cancelExpress(JtCancelExpressParam param);

}

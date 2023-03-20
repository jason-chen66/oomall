package cn.edu.xmu.oomall.freight.service.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.freight.service.openfeign.SfParam.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mockexpress-service",contextId = "sf")
public interface SfExpressService {
    /**
     * 创建运单
     * */
    @PostMapping("/sfexpress/std/service/EXP_RECE_CREATE_ORDER")
    public InternalReturnObject<SfCreateExpressRetObj> createExpress(SfCreateExpressParam param) ;

    /**
     * 查询运单(未收到中通返回信息)
     * */
    @PostMapping("/sfexpress//std/service/EXP_RECE_SEARCH_ORDER_RESP")
    public InternalReturnObject<SfGetExpressRetObj> getExpressByOrderId(SfGetExpressParam param) ;

    /**
     * 查询物流
     * */

    @PostMapping("/sfexpress//std/service/EXP_RECE_SEARCH_ROUTES")
    public InternalReturnObject<SfGetRouteRetObj> getRoute(SfGetRouteParam param);

    /**
     * 取消运单
     * */

    @PostMapping("/sfexpress//std/service/EXP_RECE_UPDATE_ORDER")
    public InternalReturnObject<SfCancelExpressRetObj> cancelExpress(SfCancelExpressParam param);

}

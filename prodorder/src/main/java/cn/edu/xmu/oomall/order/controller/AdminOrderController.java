package cn.edu.xmu.oomall.order.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.controller.vo.ShopOrderVo;
import cn.edu.xmu.oomall.order.service.AdminOrderService;
import cn.edu.xmu.oomall.order.service.dto.OrderInfoDto;
import cn.edu.xmu.oomall.order.service.dto.SimpleOrderInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @Autowired
    public AdminOrderController(AdminOrderService adminOrderService) {
        this.adminOrderService = adminOrderService;
    }

    /**
     * 店家查询名下订单 (概要)
     *
     * @return 订单概要列表
     */
    @GetMapping("/orders")
    @Audit(departName = "shops")
    public ReturnObject retrieveOrders(@PathVariable Long shopId,
                                       @RequestParam(required = false) String orderSn,
                                       @RequestParam(required = false) Long customerId,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beginTime,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                       @RequestParam(required = false, defaultValue = "1") Integer page,
                                       @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        PageDto<SimpleOrderInfoDto> pageDto = adminOrderService.retrieveOrders(shopId, customerId, orderSn, beginTime, endTime, page, pageSize);
        return new ReturnObject(pageDto);
    }

    /**
     * 店家询订单完整信息（普通，团购，预售）。
     * <p>0 普通，1团购， 2预售</p>
     */
    @GetMapping("orders/{id}")
    @Audit(departName = "shops")
    public ReturnObject findOrderById(@PathVariable Long shopId,
                                      @PathVariable Long id,
                                      @LoginUser UserDto admin) {

        OrderInfoDto dto = adminOrderService.findOrder(shopId, id,admin);
        return new ReturnObject(dto);
    }

    /**
     * 店家修改订单 (留言)
     */
    @PutMapping("/orders/{id}")
    @Audit(departName = "shops")
    public ReturnObject updateOrder(@PathVariable Long shopId,
                                    @PathVariable Long id,
                                    @RequestBody ShopOrderVo vo,
                                    @LoginUser UserDto admin) {

        adminOrderService.updateOrder(shopId, id, vo.getMessage(), admin);
        return new ReturnObject();
    }

    /**
     * 商家取消店铺下订单。
     * 需要登录
     * 商铺管理员调用本 API，只能取消店铺的订单
     * 发货前取消
     */
    @DeleteMapping("/orders/{id}")
    @Audit(departName = "shops")
    public ReturnObject deleteOrder(@PathVariable Long shopId,
                                    @PathVariable Long id,
                                    @LoginUser UserDto admin) {

        adminOrderService.cancelOrder(shopId, id, admin);
        return new ReturnObject();
    }

    /**
     * 店家确认订单
     * <p>用此 API 将一个状态为待发货的订单改为待收货，并记录运单信息</p>
     */
    @PutMapping("/orders/{id}/confirm")
    @Audit(departName = "shops")
    public ReturnObject confirmOrder(@PathVariable Long shopId,
                                     @PathVariable Long id,
                                     @LoginUser UserDto admin) {

        adminOrderService.confirmOrder(shopId, id, admin);
        return new ReturnObject();
    }
}

//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.controller.vo.OrderVo;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.controller.vo.OrderPayVo;
import cn.edu.xmu.oomall.order.service.OrderService;
import cn.edu.xmu.oomall.order.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    private final OrderService orderService;

    @Autowired
    public CustomerController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 买家申请建立订单（普通，团购，预售）
     *
     * <p>需要检查各类的id是否存在，如果id不存在，则无此对象处理</p>
     *
     * @param orderVo 订单信息
     * @param user    用户信息
     * @return 创建结果
     */
    @PostMapping("/orders")
    @Audit(departName = "shops")
    public ReturnObject createOrder(@RequestBody @Validated OrderVo orderVo,
                                    @LoginUser UserDto user) {

        orderService.createOrder(
                orderVo.getOrderItems(),
                ConsigneeDto.builder().name(orderVo.getConsignee().getName()).address(orderVo.getConsignee().getAddress()).regionId(orderVo.getConsignee().getRegionId()).mobile(orderVo.getConsignee().getMobile()).build(),
                orderVo.getMessage(),
                user);

        return new ReturnObject(ReturnNo.CREATED);
    }

    /**
     * 获取订单所有状态（大&小）
     *
     * @return 所有状态
     */
    @GetMapping("/orders/states")
    public ReturnObject retrieveOrderStates() {
        return new ReturnObject(orderService.retrieveOrderStates());
    }

    /**
     * 买家查询名下订单 (概要)。
     *
     * <p>允许买家用户（Customer）的使用者使用本 API 查询他自己的订单（普通，团购，预售）</p>
     * <p>不会返回已经被逻辑删除的订单</p>
     *
     * @return 订单列表
     */
    @GetMapping("/orders")
    @Audit(departName = "shops")
    public ReturnObject retrieveOrders(@RequestParam(required = false) String orderSn,
                                       @RequestParam(required = false) Integer status,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beginTime,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                       @RequestParam(required = false, defaultValue = "1") Integer page,
                                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                       @LoginUser UserDto user) {

        logger.debug("beginTime: {}", beginTime);
        PageDto<SimpleOrderInfoDto> pageDto = orderService.retrieveOrders(orderSn, status, beginTime, endTime, page, pageSize, user);
        return new ReturnObject(pageDto);
    }

    /**
     * 买家查询订单完整信息（普通，团购，预售）
     */
    @GetMapping("/orders/{id}")
    @Audit(departName = "shops")
    public ReturnObject findOrder(@PathVariable @NotNull Long id,
                                  @LoginUser UserDto customer) {

        OrderInfoDto dto = orderService.findOrder(id, customer);
        return new ReturnObject(dto);
    }

    /**
     * 买家修改本人名下订单
     *
     * <p>用户本人调用本 API，只能修改本人的订单</p>
     * <p>现在仅允许用户调用本 API 更改未发货订单的收货地址</p>
     */
    @PutMapping("/orders/{id}")
    @Audit(departName = "shops")
    public ReturnObject updateOrder(@PathVariable @NotNull Long id,
                                    @RequestBody ConsigneeDto consignee,
                                    @LoginUser UserDto customer) {

        orderService.updateOrder(id,consignee.getName(),consignee.getMobile(), consignee.getRegionId(), consignee.getAddress(), customer);
        return new ReturnObject();
    }

    /**
     * 买家取消本人名下订单
     *
     * <p>用户本人调用本 API，只能取消本人的订单</p>
     * <p>发货前取消</p>
     */
    @DeleteMapping("/orders/{id}")
    @Audit(departName = "shops")
    public ReturnObject deleteOrder(@PathVariable @NotNull Long id,
                                    @LoginUser UserDto customer) {

        orderService.cancelOrder(id, customer);
        return new ReturnObject();
    }

    /**
     * 买家支付订单
     * 需要登录
     */
    @PutMapping("/orders/{id}/pay")
    @Audit(departName = "shops")
    public ReturnObject payOrder(@PathVariable @NotNull Long id,
                                 @RequestBody OrderPayVo vo,
                                 @LoginUser UserDto customer) {

        orderService.payOrder(id, vo.getPoint(), vo.getShopChannel(), vo.getCoupons(),customer);
        return new ReturnObject();
    }
}

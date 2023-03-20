package cn.edu.xmu.oomall.order.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "customer-service", qualifier = "customerDao")
public interface CustomerDao {
    /**
     * 管理员查看任意买家信息 shopId只能为0
     *
     * @param shopId
     * @param id
     * @return
     */
    @GetMapping("/shops/{shopId}/customer/{id}")
    InternalReturnObject<CustomerDto> findCustomer(@PathVariable Long shopId, @PathVariable Long id);

    /**
     * 顾客查看自己的优惠券获取简要信息
     *
     * @param status 优惠卷状态
     * @param actId  优惠卷所属的活动id
     * @return
     */
    @GetMapping("/coupons")
    InternalReturnObject<List<SimpleCouponDto>> retrieveCouponsByActivityId(@RequestParam Integer status, @RequestParam Long actId);

    /**
     * 管理员查看指定优惠券信息
     *
     * @param shopId 0（管理员）
     * @param id     优惠卷id
     * @return
     */
    @GetMapping("shops/{shopId}/coupons/{id}")
    InternalReturnObject<CouponDto> findCouponById(@PathVariable Long shopId, @PathVariable Long id);

}

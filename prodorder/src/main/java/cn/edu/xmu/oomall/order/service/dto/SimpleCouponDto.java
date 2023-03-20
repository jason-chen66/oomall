package cn.edu.xmu.oomall.order.service.dto;


import cn.edu.xmu.oomall.order.dao.openfeign.dto.CouponActivityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 优惠券概要信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleCouponDto implements Serializable {

    /**
     * 优惠券id
     */
    private Long id;
    /**
     * 优惠活动
     */
    private CouponActivityDto activity;
    /**
     * 优惠券名称
     */
    private String name;
    /**
     * 优惠券编号
     */
    private String couponSn;
}

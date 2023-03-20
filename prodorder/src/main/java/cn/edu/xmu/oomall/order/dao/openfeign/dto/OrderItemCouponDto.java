package cn.edu.xmu.oomall.order.dao.openfeign.dto;


import cn.edu.xmu.oomall.order.service.dto.SimpleCouponDto;
import lombok.*;

import java.io.Serializable;

/**
 * 优惠券完整信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class OrderItemCouponDto extends SimpleCouponDto implements Serializable {

    private Long id;

    private String name;

    private CouponActivityDto activity;

    private String couponSn;
}

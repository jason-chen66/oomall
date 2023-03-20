package cn.edu.xmu.oomall.order.dao.openfeign.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponActivityDto {

    /**
     * 活动 id
     */
    private Long id;

    /**
     * 优惠活动名称
     */
    private String name;

    /**
     * 商店
     */
    private IdNameTypeDto shop;

    /**
     * 优惠券数目，0-不用优惠券
     */
    private Integer quantity;

    /**
     * 0-每人数量，1-总数控制
     */
    private Integer quantityType;

    /**
     * 从领卷起的有效天数，0-与活动同
     */
    private Integer validTerm;

    /**
     * 开始领优惠卷时间
     */
    private LocalDateTime couponTime;

}

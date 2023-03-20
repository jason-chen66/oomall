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
public class CouponDto {
    private Long id;

    private String couponSn;

    private String name;

    private Long activityId;

    private Long customerId;

    /**
     * 0 未使用 1 已使用
     */
    private Byte status;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

}

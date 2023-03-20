package cn.edu.xmu.oomall.freight.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 快递公司无法配送的地区
 */
@Data
@NoArgsConstructor
public class ShopUndeliverableRegionVo {
    @NotNull(message = "开始时间必填")
    private LocalDateTime beginTime;
    @NotNull(message = "结束时间必填")
    private LocalDateTime endTime;



}

package cn.edu.xmu.oomall.freight.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class WarehouseRegionVo {
    @NotNull(message = "起效时间必填")
    private LocalDateTime beginTime;

    @NotNull(message = "终止时间必填")
    private LocalDateTime endTime;
}

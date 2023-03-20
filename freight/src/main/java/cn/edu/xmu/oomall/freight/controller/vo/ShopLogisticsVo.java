package cn.edu.xmu.oomall.freight.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 物流合作信息
 */
@Data
@NoArgsConstructor
public class ShopLogisticsVo {

    private Long logisticsId;
    @NotNull(message = "店铺物流secret不能为空")
    private String secret;
    @NotNull(message = "店铺物流优先级不能为空")
    private Integer priority;
}

package cn.edu.xmu.oomall.freight.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class WarehouseVo {
    @NotBlank(message = "仓库名称不能为空")
    private String name;

    @NotBlank(message = "详细地址不能为空")
    private String address;

    @NotNull(message = "地区id必填")
    private Long regionId;

    @NotNull(message = "联系人姓名必填")
    private String senderName;

    @NotNull(message = "联系电话必填")
    private String senderMobile;

}

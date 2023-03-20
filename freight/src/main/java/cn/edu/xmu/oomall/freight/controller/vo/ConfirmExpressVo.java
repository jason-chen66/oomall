package cn.edu.xmu.oomall.freight.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ConfirmExpressVo {

    /* 验收状态 0-验收不合格 1-验收合格 */
    @NotNull(message = "验收状态不应为空")
    private int status;
}

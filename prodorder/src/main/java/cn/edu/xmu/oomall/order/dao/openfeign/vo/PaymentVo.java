package cn.edu.xmu.oomall.order.dao.openfeign.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static cn.edu.xmu.javaee.core.model.Constants.BEGIN_TIME;
import static cn.edu.xmu.javaee.core.model.Constants.END_TIME;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVo {

    @NotNull(message="开始时间不能为空")
    private LocalDateTime timeBegin = BEGIN_TIME;

    @NotNull(message="结束时间不能为空")
    private LocalDateTime timeExpire = END_TIME;

    @NotNull(message="支付渠道必填")
    private Long shopChannelId;

    @Min(value = 0, message="付款金额需大于等于0")
    private Long amount;

    @Min(value = 0, message="付款分账金额需大于等于0")
    private Long divAmount;

    /**
     * 支付用户标识
     */
    private String spOpenid;

}
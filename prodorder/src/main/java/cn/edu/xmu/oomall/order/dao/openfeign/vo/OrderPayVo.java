//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao.openfeign.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static cn.edu.xmu.javaee.core.model.Constants.BEGIN_TIME;
import static cn.edu.xmu.javaee.core.model.Constants.END_TIME;

/**
 * 支付的vo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPayVo {


    private LocalDateTime timeBegin = BEGIN_TIME;


    private LocalDateTime timeExpire = END_TIME;


    private Long shopChannelId;


    private Long amount;


    private Long divAmount;

    /**
     * 支付用户标识
     */
    private String spOpenid;

}

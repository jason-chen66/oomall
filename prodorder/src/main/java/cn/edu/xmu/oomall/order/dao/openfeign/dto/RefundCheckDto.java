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
public class RefundCheckDto {
    /**
     * 待退款
     */
    public static final Byte NEW = 0;
    /**
     * 已退款
     */
    public static final Byte SUCCESS = 1;
    /**
     * 已对账
     */
    public static final Byte CHECKED = 2;
    /**
     * 错账
     */
    public static final Byte WRONG = 3;
    /**
     * 退款失败
     */
    public static final Byte FAIL = 4;
    /**
     * 退款取消
     */
    public static final Byte CANCEL = 5;
    private Long id;
    private String outNo;
    private String transNo;
    private Long amount;
    private LocalDateTime successTime;
    private Byte status;
    private String userReceiveAccount;
    private IdNameDto creator;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private IdNameDto modifier;
}

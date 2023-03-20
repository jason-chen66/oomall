package cn.edu.xmu.oomall.order.dao.openfeign.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentCheckDto {

    private Long id;

    private String outNo;

    private String transNo;

    private Long amount;

    private LocalDateTime successTime;

    private String prepayId;

    private Byte status;

    private LocalDateTime timeBegin;

    private LocalDateTime timeExpire;

    private IdNameDto adjustor;

    private LocalDateTime adjustTime;

    private IdNameDto creator;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private IdNameDto modifier;
}

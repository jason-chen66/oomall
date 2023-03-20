package cn.edu.xmu.oomall.order.dao.openfeign.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResultDto {
    private Long id;
    /**
     * 创建者id
     */
    private Long creatorId;


    /**
     * 创建者
     */
    private String creatorName;
    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;
}

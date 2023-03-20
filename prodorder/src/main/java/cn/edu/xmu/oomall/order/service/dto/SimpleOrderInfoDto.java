package cn.edu.xmu.oomall.order.service.dto;

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
public class SimpleOrderInfoDto {
    private Long id;
    private Integer status;
    private LocalDateTime gmtCreate;
    private Long originPrice;
    private Long discountPrice;
    private Long freightPrice;
}

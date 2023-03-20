package cn.edu.xmu.oomall.order.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 顾客概要信息
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Builder
public class SimpleCustomerDto {
    /**
     * 顾客id
     */
    private Long id;
    /**
     * 顾客名称
     */
    private String name;
}

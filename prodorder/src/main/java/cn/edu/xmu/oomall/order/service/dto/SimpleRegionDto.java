package cn.edu.xmu.oomall.order.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地区概要信息
 * */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleRegionDto {
    /**
     * 地区id
     * */
    private Long id;
    /**
     * 地区名称
     * */
    private String name;
}

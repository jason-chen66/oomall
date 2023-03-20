package cn.edu.xmu.oomall.order.dao.openfeign.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 活动概要信息
 * */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemActivityDto {
    /**
     * 活动id
     * */
    private Long id;
    /**
     * 活动名称
     * */
    private String name;
    /**
     * 活动类型
     * */
    private Byte type;
}

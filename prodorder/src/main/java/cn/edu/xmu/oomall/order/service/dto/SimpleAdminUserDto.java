package cn.edu.xmu.oomall.order.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员概要信息
 * */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleAdminUserDto {
    /**
     * 管理员id
     * */
    private Long id;
    /**
     * 管理员名称
     * */
    private String name;
}

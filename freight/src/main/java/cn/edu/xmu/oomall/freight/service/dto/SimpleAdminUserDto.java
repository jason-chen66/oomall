package cn.edu.xmu.oomall.freight.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleAdminUserDto {
    /**
     * format:int64
     */
    private Long id;

    /**
     * 管理员姓名
     */
    private String userName;
}

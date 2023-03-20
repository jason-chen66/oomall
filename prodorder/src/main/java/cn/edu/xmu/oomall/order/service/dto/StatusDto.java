package cn.edu.xmu.oomall.order.service.dto;



import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 状态DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusDto {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 状态名称
     */
    private String name;
}

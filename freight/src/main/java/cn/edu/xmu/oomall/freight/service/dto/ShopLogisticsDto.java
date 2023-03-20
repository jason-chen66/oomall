package cn.edu.xmu.oomall.freight.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopLogisticsDto {

    private Long id;

    private SimpleLogisticsDto logistics;

    private byte status;

    private String secret;

    private int priority;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private SimpleAdminUserDto creator;

    private SimpleAdminUserDto modifier;
}

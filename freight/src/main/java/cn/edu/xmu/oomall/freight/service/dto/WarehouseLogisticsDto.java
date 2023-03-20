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
public class WarehouseLogisticsDto {

    private ShopLogisticsDto shopLogistics;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Byte invalid;

    private SimpleAdminUserDto creator;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private SimpleAdminUserDto modifier;

}

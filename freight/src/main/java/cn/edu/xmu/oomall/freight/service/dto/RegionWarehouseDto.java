package cn.edu.xmu.oomall.freight.service.dto;

import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionWarehouseDto {

    private SimpleWarehouseDto warehouse;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private SimpleAdminUserDto creator;

    private SimpleAdminUserDto modifier;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
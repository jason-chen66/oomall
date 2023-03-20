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
public class WarehouseRegionDto {

    private SimpleRegionDto region;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private SimpleAdminUserDto creator;

    private SimpleAdminUserDto modifier;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}

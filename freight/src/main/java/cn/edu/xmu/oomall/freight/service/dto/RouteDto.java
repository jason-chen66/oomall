package cn.edu.xmu.oomall.freight.service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RouteDto {
    /**
     * 物流内容
     */
    private String content;

    /**
     * format: "datetime"
     * description: "创建时间"
     */
    private LocalDateTime gmtCreate;
}

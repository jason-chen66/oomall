package cn.edu.xmu.oomall.order.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRefundResultDto {
    private Long id;
    private Long creatorId;
    private String creatorName;
    private LocalDateTime gmtCreate;
    @Builder
    OrderRefundResultDto(Long id,Long creatorId,String creatorName,LocalDateTime gmtCreate){
        this.id = id;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.gmtCreate = gmtCreate;
    }
}
package cn.edu.xmu.oomall.order.service.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商铺概要信息
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Builder
public class SimpleShopDto {
    /**
     * 商铺id
     */
    private Long id;
    /**
     * 商铺名称
     */
    private String name;
    /**
     * 商铺类型 0-电商；1-服务商
     */
    private Byte type;

    private Integer status;
}

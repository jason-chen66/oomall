package cn.edu.xmu.oomall.order.dao.openfeign.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Builder
public class DiscountDto {

    /**
     * onsale id
     */
    private Long id;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 价格
     */
    private Long price;

    /**
     * 优惠价格
     */
    private Long discount;
}

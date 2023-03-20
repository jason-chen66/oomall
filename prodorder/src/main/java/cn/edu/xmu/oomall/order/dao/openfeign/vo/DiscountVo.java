package cn.edu.xmu.oomall.order.dao.openfeign.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;


@AllArgsConstructor
@Builder
public class DiscountVo {

    /**
     * 销售id
     */
    private Long id;

    /**
     * 数量
     */
    private Integer quantity;
}

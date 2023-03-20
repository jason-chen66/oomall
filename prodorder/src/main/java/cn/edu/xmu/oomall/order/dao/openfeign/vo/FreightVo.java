package cn.edu.xmu.oomall.order.dao.openfeign.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class FreightVo {

    private Long orderItemId;

    private Long productId;

    private Integer quantity;

    private Long weight;
}

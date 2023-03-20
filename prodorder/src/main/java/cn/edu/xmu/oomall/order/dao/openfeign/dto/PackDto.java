package cn.edu.xmu.oomall.order.dao.openfeign.dto;

import lombok.Data;

@Data
public class PackDto {

    private Long orderItemId;

    private Long productId;

    private Integer quantity;
}

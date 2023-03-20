package cn.edu.xmu.oomall.order.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemInfoVo {
     Long orderItemId;
     Long productId;
     Long quantity;
     Long weight;
}

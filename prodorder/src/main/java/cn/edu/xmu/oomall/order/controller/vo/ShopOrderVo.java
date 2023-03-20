package cn.edu.xmu.oomall.order.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商铺修改订单（留言）
 * */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopOrderVo {
    /**
     * 留言信息
     * */
    private String message;
}

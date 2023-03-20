//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service.dto;

import cn.edu.xmu.oomall.order.dao.openfeign.dto.OrderItemActivityDto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OrderItemDto implements Serializable {

    /**
     * 货品id
     */
    private Long productId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 商品被下单时的名称
     */
    private String name;

    /**
     * 订购数量
     */
    private Integer quantity;

    /**
     * 下单时商品单价
     */
    private Long price;

    /**
     * 优惠后单价
     */
    private Long discountPrice;

    /**
     * 参与活动
     */
    private OrderItemActivityDto activity;

    /**
     * 优惠券
     */
    private SimpleCouponDto coupon;

}

package cn.edu.xmu.oomall.order.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
/**
 * 指定新订单的资料
 * */
public class OrderInfoVo {
    /**
     * 订单内容物信息
     * */
    private List<OrderItemVo> orderItems;
    /**
     * 收货人
     * */
    private String consignee;
    /**
     * 地区id
     * */
    private Long regionId;
    /**
     * 详细地址
     * */
    private String address;
    /**
     * 手机号
     * */
    private String mobile;
    /**
     * 附言
     * */
    private String message;

}

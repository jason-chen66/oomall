package cn.edu.xmu.oomall.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderInfoDto implements Serializable {

    /**
     * 订单id
     */
    private Long id;
    /**
     * 订单序号
     */
    private String orderSn;
    /**
     * 顾客
     */
    private SimpleCustomerDto customer;
    /**
     * 商铺信息
     */
    private SimpleShopDto shop;
    /**
     * 母单id
     */
    private Long pid;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 订单创建时间
     */
    private LocalDateTime gmtCreate;
    /**
     * 订单修改时间
     */
    private LocalDateTime gmtModified;

    /**
     * 原始价格
     */
    private Long originPrice;
    /**
     * 折扣价格
     */
    private Long disCountPrice;
    /**
     * 快递费用
     */
    private Long expressFee;

    /**
     * 附言
     */
    private String message;
    /**
     * 联系人
     */
    private ConsigneeDto consignee;

    /**
     * 包裹
     */
    private SimplePackageDto pack;
    /**
     * 订单明细
     */
    private List<OrderItemDto> orderItems;

}


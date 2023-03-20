package cn.edu.xmu.oomall.order.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 指定发货资讯
 * */
@Data
@NoArgsConstructor
public class DeliverVo {
    /**
     * 商铺物流号，为0按照优先级决定
     * */
    private Integer shopLogisticId;
    /**
     * 发货人
     * */
    private ConsigneeVo sender;
    /**
     * 订单联系人
     * */
    private ConsigneeVo delivery;
}

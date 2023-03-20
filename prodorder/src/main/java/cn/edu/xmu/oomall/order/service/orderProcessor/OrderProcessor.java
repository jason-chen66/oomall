package cn.edu.xmu.oomall.order.service.orderProcessor;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.dao.bo.Order;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface OrderProcessor {
    public final Byte SHOP_CANCEL=0;
    public final Byte CUSTOMER_CANCEL=1;

    public final Byte ACT_CANCEL=2;
    /**
     * 支付导致的状态迁移
     */
    void orderPayStatus(Order order);

    /**
     * 退款处理
     */
    void orderRefundStatus(Order order, Byte cancelType, UserDto userDto);

}

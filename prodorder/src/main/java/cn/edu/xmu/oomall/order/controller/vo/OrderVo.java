//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderVo {

    @NotNull(message = "订单信息不能为空")
    private List<OrderItemVo> orderItems;

    @NotNull(message = "收货信息不能为空")
    private ConsigneeVo consignee;

    private String message;

}

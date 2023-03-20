package cn.edu.xmu.oomall.mockexpress.controller.vo.ZtoParam;

import lombok.Data;

@Data
public class ZtoGetExpressParam {
    /**
     * 0，预约件 1，全网件
     */
    int type;

    /* 可选 */
    /**
     * 订单编号
     */
    String orderCode;

    /**
     * 运单编号
     */
    String billCode;
}

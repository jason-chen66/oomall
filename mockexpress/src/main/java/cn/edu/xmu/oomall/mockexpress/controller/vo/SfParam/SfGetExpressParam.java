package cn.edu.xmu.oomall.mockexpress.controller.vo.SfParam;

import lombok.Data;

@Data
public class SfGetExpressParam {
    /**
     * 客户运单号
     */
    String orderId;

    /* 可选 */
    /**
     * 查询类型
     * 1正向单 2退货单
     */
    String searchType;
}

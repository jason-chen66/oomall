package cn.edu.xmu.oomall.freight.service.openfeign.SfParam;

import lombok.Data;

@Data
public class SfCancelExpressParam {
    /**
     * 运单号
     */
    String orderId;

    /**
     * 处理类型
     * 默认为2 不可更改
     */
    byte dealType = 2;
}

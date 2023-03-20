package cn.edu.xmu.oomall.freight.service.openfeign.ZtoParam;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中通申请取消运单接口参数
 */
@Data
@NoArgsConstructor
public class ZtoCancelExpressParam {
    /*必选*/
    /**
     * 取消类型
     * 1不想寄了,2下错单,3重复下单,4运费太贵,5无人联系,6取件太慢,7态度差
     */
    String cancelType;

    /*二选一*/
    /**
     * 订单号
     */
    String orderCode;
    /**
     * 运单号
     */
    String billCode;
}

package cn.edu.xmu.oomall.freight.service.openfeign.JtParam;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中通申请取消运单接口参数
 */
@Data

@NoArgsConstructor
public class JtCancelExpressParam {
    /*必选*/
    /**
     * 客户编码
     * 联系出货网点提供
     */
    String customerCode;

    /**
     * 签名字符串
     * Base64(Md5(客户编号+密文+privateKey))
     * 其中密文：MD5(明文密码+jadada236t2) 后大写
     * 明文密码:H5CD3zE6
     */
    String digest;

    /**
     * 顾客订单号
     * 系统自己的订单号
     */
    String txlogisticId;

    /**
     * 订单类型
     * 1（散客），2（协议客户）
     */
    String orderType;

    /**
     * 取消原因
     */
    String reason;
}

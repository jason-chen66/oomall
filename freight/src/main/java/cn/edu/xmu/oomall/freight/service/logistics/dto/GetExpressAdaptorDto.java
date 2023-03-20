package cn.edu.xmu.oomall.freight.service.logistics.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetExpressAdaptorDto {

    /**
     * 操作状态
     * 0. 失败
     * 1. 成功
     */
    Byte status;

    /**
     * 描述
     */
    String msg;

    /**
     * 关联订单号
     */
    Long orderId;

    /**
     * 运单号
     */
    String billCode;

    /**
     * 订单状态
     * 1. 已取消
     * 2. 已发货
     * 3. 已签收
     * 4. 丢失
     */
    byte expressStatus;
}

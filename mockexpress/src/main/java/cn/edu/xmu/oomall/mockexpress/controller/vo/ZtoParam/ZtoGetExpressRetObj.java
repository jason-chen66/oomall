package cn.edu.xmu.oomall.mockexpress.controller.vo.ZtoParam;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ZtoGetExpressRetObj {
    /**
     * 返回信息
     */
    String message;
    /**
     * 错误码
     */
    String statusCode;
    /**
     * 是否成功状态
     */
    Boolean status;

    /**
     * 返回订单详情实体
     */
    List<PreOrderInfoDto> result;

    @Data
    @NoArgsConstructor
    public static class PreOrderInfoDto{
        String billCode;

        String orderCode;

        String sendName;
        String sendPhone;
        String sendMobile;
        String sendProv;
        String sendCity;
        String sendCounty;
        String sendAddress;

        String receivName;
        String receivPhone;
        String receivMobile;
        String receivProv;
        String receivCity;
        String receivCounty;
        String receivAddress;

        /**
         * 订单备注
         */
        String orderRemark;

        /**
         * 订单状态
         * 0：下单成功；
         * 1：分配网点；
         * 2：分配业务员；
         * 3：业务员上门取件；
         * -2订单取消；
         * 99：订单完成
         */
        int orderStatus;


    }

}

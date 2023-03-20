package cn.edu.xmu.oomall.mockexpress.controller.vo.SfParam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
public class SfGetExpressRetObj {
    /**
     * 错误信息
     */
    String errorMsg;
    /**
     * 错误码
     * S0000成功
     */
    String errorCode;
    /**
     * 是否成功状态
     * true/false
     */
    String success;

    /**
     * 返回的详细数据
     */
    Result msgData;

    @Data
    @NoArgsConstructor
    public static class Result {
        /**
         * 订单号
         */
        String orderId;

        /**
         * 1:客户订单号与顺丰运单不匹配 2 :操作成功
         */
        byte resStatus ;

        /* 可选 */
        /**
         * 返回的运单信息
         */
        List<WaybillNoInfoList> waybillNoInfoList;

        @Data
        @AllArgsConstructor
        public static class WaybillNoInfoList {
            /**
             * 运单号类型 1：母单 2 :子单 3 : 签回单
             */
            byte waybillType;
            /**
             * 订单号
             */
            String waybillNo;
        }
    }
}

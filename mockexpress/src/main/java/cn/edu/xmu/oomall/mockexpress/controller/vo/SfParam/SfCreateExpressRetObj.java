package cn.edu.xmu.oomall.mockexpress.controller.vo.SfParam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 中通创建运单返回参数
 */
@Data
@NoArgsConstructor
public class SfCreateExpressRetObj {
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
         * 返回的运单信息
         */
        List<WaybillNoInfoList> waybillNoInfoList;

        @Data
        @AllArgsConstructor
        public static class WaybillNoInfoList{
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

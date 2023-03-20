package cn.edu.xmu.oomall.mockexpress.controller.vo.ZtoParam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中通创建运单返回参数
 */
@Data
@NoArgsConstructor
public class ZtoCreateExpressRetObj {
    /**
     * 错误信息
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

    Result result;

    @Data
    @NoArgsConstructor
    public static class Result {

        SignBillInfo signBillInfo;
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SignBillInfo {
            /**
             * 平台订单号
             */
                String orderCode;
            /**
             * 运单号
             */
            String billCode;
            /**
             * 合作伙伴码
             */
            String partnerOrderCode;
        }
    }
}

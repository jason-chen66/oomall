package cn.edu.xmu.oomall.freight.service.openfeign.JtParam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中通创建运单返回参数
 */
@Data
@NoArgsConstructor
public class JtCreateExpressRetObj {
    /**
     * 错误信息
     */
    String msg;
    /**
     * 错误码
     */
    String code;
    /**
     * 业务数据
     */
    Result data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        /**
         * 集包地
         */
        String lastCenterName;

        /**
         * 平台订单号
         */
        String txlogisticId;

        /**
         * 创建时间
         * 格式：yyyy-MM-dd HH:mm:ss
         */
        String createOrderTime;

        /**
         * 运单号
         */
        String billCode;

        /**
         * 三段码（获取到三段码优先返回三段码，无三段码返回大头笔）
         */
        String sortingCode;
    }

    public JtCreateExpressRetObj(String msg, String code, Result data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }
}

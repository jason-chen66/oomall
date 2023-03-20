package cn.edu.xmu.oomall.freight.service.openfeign.JtParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 中通查询物流轨迹接口响应参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JtGetRouteRetObj {
    /**
     * 返回信息
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
    public static class Result{
        /**
         * 运单号
         */
        String billCode;

        List<JtRouteResultParam> details;

    }


}

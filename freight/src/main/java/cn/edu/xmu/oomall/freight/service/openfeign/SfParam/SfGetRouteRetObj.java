package cn.edu.xmu.oomall.freight.service.openfeign.SfParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 中通查询物流轨迹接口响应参数
 */
@Data
@NoArgsConstructor
public class SfGetRouteRetObj {
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
    public static class Result{
        List<SfRouteResultParam> routeResps;
    }

}

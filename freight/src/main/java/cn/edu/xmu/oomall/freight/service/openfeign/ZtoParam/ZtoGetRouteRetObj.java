package cn.edu.xmu.oomall.freight.service.openfeign.ZtoParam;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 中通查询物流轨迹接口响应参数
 */
@Data
@NoArgsConstructor
public class ZtoGetRouteRetObj {
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
     * 物流轨迹返回结果
     */
    List<ZtoRouteResultParam> result;
}

package cn.edu.xmu.oomall.freight.service.openfeign.ZtoParam;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中通查询物流轨迹信息
 */
@Data
@NoArgsConstructor
public class ZtoGetRouteParam {
    /*必选参数*/
    /**
     * 运单号
     */
    String billCode;
}

package cn.edu.xmu.oomall.mockexpress.controller.vo.ZtoParam;

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

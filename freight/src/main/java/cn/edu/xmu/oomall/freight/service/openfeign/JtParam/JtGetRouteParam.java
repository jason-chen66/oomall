package cn.edu.xmu.oomall.freight.service.openfeign.JtParam;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中通查询物流轨迹信息
 */
@Data
@NoArgsConstructor
public class JtGetRouteParam {
    /*必选参数*/
    /**
     * 运单号
     * 多个运单号以英文逗号隔开
     * 目前先支持一次性最多查询30票运单
     */
    String billCodes;
}

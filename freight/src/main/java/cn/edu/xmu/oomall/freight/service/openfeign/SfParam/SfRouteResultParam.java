package cn.edu.xmu.oomall.freight.service.openfeign.SfParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 中通物流轨迹参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SfRouteResultParam {
    /**
     * 顺丰运单号
     */
    String mailNo;

    /**
     * 路由信息（列表）
     */
    List<Route> routes;

    @Data
    @AllArgsConstructor
    public static class Route{
        /**
         * 路由节点发生的时间，
         * 格式：YYYY-MM-DD HH24:MM:SS，
         * 示例：2012-7-30 09:30:00
         */
        String acceptTime;

        /**
         * 路由节点发生的地点
         * 非必传
         */
        String acceptAddress;

        /**
         * 路由节点具体描述
         */
        String remark;

        /**
         * 路由节点操作码
         */
        String opcode;
    }
}

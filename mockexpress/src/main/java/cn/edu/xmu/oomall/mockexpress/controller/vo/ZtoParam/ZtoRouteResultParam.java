package cn.edu.xmu.oomall.mockexpress.controller.vo.ZtoParam;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 中通物流轨迹参数
 */
@Data
@NoArgsConstructor
public class ZtoRouteResultParam {
    /**
     * 快件所在国家
     */
    String country;
    /**
     * 签收人
     */
    String signMan;
    /**
     * 操作人
     */
    String operateUser;
    /**
     * 操作人联系方式
     */
    String operateUserPhone;
    /**
     * 运单号
     */
    String billCode;
    /**
     * 扫描类型:
     * 收件 、发件、 到件、 派件、 签收、退件、问题件、ARRIVAL（派件入三方自提柜等、SIGNED（派件出三方自提柜等）
     */
    String scanType;

    PreOrNextSite preSite;
    /**
     * 上、下一站网点信息（scanType为发件，表示下一站；scanType为到件，表示上一站）
     */
    @Data
    @NoArgsConstructor
    public static class PreOrNextSite {
        /**
         * 网点电话
         */
        String phone;
        /**
         * 网点是否为转运中心
         */
        Integer isTransfer;
        /**
         * 网点名称
         */
        String name;

        /**
         * 网点所在省份
         */
        String prov;
    }

    /**
     * 扫描时间
     */
    String scanDate;
    /**
     * 扫描网点
     */
    ScanSite scanSite;
    @Data
    @NoArgsConstructor
    public static class ScanSite{
        /**
         * 网点电话
         */
        String phone;
        /**
         * 网点是否为转运中心
         */
        Integer isTransfer;
        /**
         * 网点名称
         */
        String name;

        /**
         * 网点所在省份
         */
        String prov;
    }
    /**
     * 轨迹描述信息
     */
    String desc;

}

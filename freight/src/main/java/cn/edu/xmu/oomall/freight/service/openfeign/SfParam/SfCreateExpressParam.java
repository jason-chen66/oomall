package cn.edu.xmu.oomall.freight.service.openfeign.SfParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
public class SfCreateExpressParam {
    /**
     * 响应报文的语言
     * 缺省值为zh-CN
     * 目前支持以下值
     * zh-CN 表示中文简体，
     * zh-TW或zh-HK或 zh-MO表示中文繁体，
     * en表示英文
     */
    String language = "zh-CN";

    /**
     * 客户订单号，不能重复
     */
    String orderId;

    /**
     * 托寄物信息
     */
    List<CargoDetail> cargoDetails;

    /**
     * 收发件人信息
     */
    List<ContactInfo> contactInfoList;

    /**
     * 快件类型
     */
    int expressTypeId;

    /**
     * 是否返回路由标签
     */
    byte isReturnRoutelabel = 1;

    /* 可选 */
    /**
     * 总重
     */
    long totalWeight;

    /**
     * 温度范围
     * 当 express_type为12 医药温控件时必填，
     * 支持以下值： 1:冷藏 3：冷冻
     */
    int temperatureRange;

    @Data
    @NoArgsConstructor
    public class CargoDetail{
        /* 必填 */
        /**
         * 货物名称，如果需要生成电子 运单，则为必填
         */
        String  name;

        /* 非必填 */
        /**
         * 货物数量 跨境件报关需要填写
         */
        int count;
        /**
         * 货物单位，如：个、台、本， 跨境件报关需要填写
         */
        String unit;
        /**
         * 条件	订单货物单位重量，包含子母件， 单位千克，精确到小数点后3位 跨境件报关需要填写
         */
        int	weight;
        /**
         * 条件	货物单价，精确到小数点后3位， 跨境件报关需要填写
         */
        int amount;
        /**
         * 货物单价的币别
         */
        String currency;
        /**
         * 原产地国别，跨境件报关需要填写
         */
        String sourceArea;
    }

    @Data
    @NoArgsConstructor
    public static class ContactInfo{
        /**
         * 地址类型
         * 1 寄件方信息
         * 2 到件方信息
         */
        byte contactType;

        /**
         * 联系人
         */
        String contact;

        /**
         * 联系电话
         */
        String tel;

        /**
         * 手机
         */
        String mobile;

        /**
         * 国家或地区的2位码
         * 默认中国CN
         */
        String country = "CN";

        /**
         * 所在省
         */
        String province;

        /**
         * 所在市
         */
        String city;

        /**
         * 所在县/区
         */
        String county;

        /**
         * 详细地址
         */
        String address;

    }

}

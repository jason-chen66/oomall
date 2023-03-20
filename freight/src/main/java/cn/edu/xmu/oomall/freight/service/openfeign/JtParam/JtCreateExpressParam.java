package cn.edu.xmu.oomall.freight.service.openfeign.JtParam;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中通创建运单接口参数
 */
@Data
@NoArgsConstructor
public class JtCreateExpressParam {
    /*必选*/
    /**
     * 客户编码
     * 联系出货网点提供
     */
    String customerCode;

    /**
     * 签名字符串
     * Base64(Md5(客户编号+密文+privateKey))
     * 其中密文：MD5(明文密码+jadada236t2) 后大写
     * 明文密码:H5CD3zE6
     */
    String digest;

    /**
     * 顾客订单号
     * 系统自己的订单号
     */
    String txlogisticId;

    /**
     * 快件类型
     * EZ:标准快递
     */
    String expressType;

    /**
     * 订单类型 有客户编号则为月结
     * 1. 散客； 2. 月结
     * channel.app_id
     * */
    String orderType;

    /**
     * 服务类型
     * 02 门店寄件 ； 01 上门取件
     */
    String serverType;

    /**
     * 派送类型
     * 06 代收点自提 05 快递柜自提 04 站点自提 03 派送上门
     */
    String deliveryType;

    /**
     * 支付方式
     * PP_PM("寄付月结"), CC_CASH("到付现结");
     */
    String payType;

    /**
     * 物品类型
     * bm000001:文件;bm000002:数码产品;bm000003:生活用品;bm000004:食品;
     * bm000005:服饰;bm000006:其他;bm000007:生鲜;bm000008:易碎品;
     * bm000009:液体;
     */
    String goodsType;

    /**
     * 寄件信息对象
     */
    Sender sender;

    /**
     * 收件信息对象
     */
    Receiver receiver;

    /**
     * 发件人信息
     */
    @Data
    @NoArgsConstructor
    public static class Sender {
        /*必选参数*/
        String name;
        /* mobile phone 二选一 */
        String mobile;
        String phone;

        /**
         * 寄件国家三字码（如：中国=CHN、印尼=IDN）
         */
        String countryCode = "CHN";
        String prov;
        String city;
        String area;
        String address;
    }

    /**
     * 收件人信息
     */
    @Data
    @NoArgsConstructor
    public static class Receiver {
        /*必选参数*/
        String name;
        /* mobile phone 二选一 */
        String mobile;
        String phone;

        /**
         * 寄件国家三字码（如：中国=CHN、印尼=IDN）
         * 默认中国
         */
        String countryCode = "CHN";
        String prov;
        String city;
        String area;
        String address;
    }

    /**
     * 重量
     * 单位kg
     * 范围：0.01~30
     * 默认：0.02
     */
    String weight = "0.02";

    /* 非必选 */
    String remark;
}

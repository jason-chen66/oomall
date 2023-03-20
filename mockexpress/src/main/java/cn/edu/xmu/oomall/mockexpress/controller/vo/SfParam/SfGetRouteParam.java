package cn.edu.xmu.oomall.mockexpress.controller.vo.SfParam;

import lombok.Data;

import java.util.List;

@Data
public class SfGetRouteParam {
    /* 必选 */
    /**
     * 查询号类别:
     * 1:根据顺丰运单号查询,trackingNumber将被当作顺丰运单号处理
     * 2:根据客户订单号查询,trackingNumber将被当作客户订单号处理
     */
    int trackingNum;

    /**
     * 查询号:
     * trackingType=1,则此值为顺丰运单号
     * 如果trackingType=2,则此值为客户订单号
     */
    List<String> trackingNumber;

    /* 可选 */
    /**
     *  响应报文的语言，
     *  缺省值为zh-CN，
     *  目前支持以下值
     *  zh-CN 表示中文简体，
     *  zh-TW或zh-HK或 zh-MO表示中文繁体，
     *  en表示英文
     */
    String language = "zh-CN";

    /**
     * 电话验证
     */
    String checkPhoneNo;

    /**
     * 路由查询类别:
     * 1:标准路由查询
     * 2:定制路由查询
     */
    byte methodType;
}

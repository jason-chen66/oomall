package cn.edu.xmu.oomall.mockexpress.controller.vo.ZtoParam;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中通创建运单接口参数
 */
@Data
@NoArgsConstructor
public class ZtoCreateExpressParam {
    /*必选*/
    /**
     * 合作类型 1：集团客户；2：非集团客户
     * */
    String partnerType;

    /**
     * 订单类型
     * partnerType为1 ：1：全网件 2：预约件
     * partnerType为2 ：1：全网件 2：预约件（返回运单号） 3：预约件（不返回运单号） 4：星联全网件
     * channel.app_id
     * */
    String orderType;

    /**
     * 合作商订单号
     */
    String partnerOrderCode;

    /**
     * 发件人信息
     */
    Sender senderInfo;
    @Data
    @NoArgsConstructor
    public static class Sender {
        /*必选参数*/
        String senderMobile;
        String senderName;
        String senderAddress;
        String senderDistrict;
        String senderProvince;
        String senderCity;
    }

    /**
     * 收件人信息
     */
    Receiver receiverInfo;
    @Data
    @NoArgsConstructor
    public static class Receiver {
        /*必选参数*/
        String receiverMobile;
        String receiverName;
        String receiverAddress;
        String receiverDistrict;
        String receiverProvince;
        String receiverCity;
    }
    /*可选*/
    /**
     * 运单号
     */
    String billCode;
}

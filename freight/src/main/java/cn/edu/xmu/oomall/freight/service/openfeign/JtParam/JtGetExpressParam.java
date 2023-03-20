package cn.edu.xmu.oomall.freight.service.openfeign.JtParam;
import lombok.Data;

import java.util.List;

@Data
public class JtGetExpressParam {
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
     * 查询命令
     * 1.按客户订单编号查询
     * 2.按运单编号查询（用运单号查订单状态）
     * 3.按下单时间段查询（查订单）（按时间段查询返回不限制条数）
     * 4.订单编号
     * 默认按运单号查询
     */
    int command = 2;

    /* 非必选 */
    /**
     * 存放查询的运单号或者系统订单号
     * command=1 客户订单编号查询 ;
     * command=2 运单编号暂时;
     * command=4订单号（公司内部）支持200个;
     * Command=1、2或3时必填
     */
    List<String> serialNumber;

    /**
     * 状态
     * （订单状态取下方状态表，筛选数据使用，默认全部）
     * 已取消 104 ;
     * 已取件 103 ;
     * 已调派业务员 102 ;
     * 已调派网点 101 ;
     * 未调派 100 ;
     */
    int status;

    /**
     * 开始时间
     * (24小时制: yyyy-MM-dd HH:mm:ss)
     * （客户下单时间）
     * command = 3 时必填
     */
    String startDate;

    /**
     * 结束时间
     * (24小时制: yyyy-MM-dd HH:mm:ss)
     * 时间范围最大7天
     * command = 3 时必填
     */
    String endDate;

    /**
     * command = 3 时必填
     */
    int current;

    /**
     * 传出的运单数目
     * command = 3 时必填
     */
    int size;

}

package cn.edu.xmu.oomall.wechatpay.model.po;

import java.time.LocalDateTime;

public class WeChatPayRefundPo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_refund.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_refund.out_refund_no
     *
     * @mbg.generated
     */
    private String outRefundNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_refund.out_trade_no
     *
     * @mbg.generated
     */
    private String outTradeNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_refund.status
     *
     * @mbg.generated
     */
    private String status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_refund.refund
     *
     * @mbg.generated
     */
    private Integer refund;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_refund.payer_total
     *
     * @mbg.generated
     */
    private Integer payerTotal;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_refund.success_time
     *
     * @mbg.generated
     */
    private LocalDateTime successTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_refund.id
     *
     * @return the value of wechatpay_refund.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_refund.id
     *
     * @param id the value for wechatpay_refund.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_refund.out_refund_no
     *
     * @return the value of wechatpay_refund.out_refund_no
     *
     * @mbg.generated
     */
    public String getOutRefundNo() {
        return outRefundNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_refund.out_refund_no
     *
     * @param outRefundNo the value for wechatpay_refund.out_refund_no
     *
     * @mbg.generated
     */
    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo == null ? null : outRefundNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_refund.out_trade_no
     *
     * @return the value of wechatpay_refund.out_trade_no
     *
     * @mbg.generated
     */
    public String getOutTradeNo() {
        return outTradeNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_refund.out_trade_no
     *
     * @param outTradeNo the value for wechatpay_refund.out_trade_no
     *
     * @mbg.generated
     */
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_refund.status
     *
     * @return the value of wechatpay_refund.status
     *
     * @mbg.generated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_refund.status
     *
     * @param status the value for wechatpay_refund.status
     *
     * @mbg.generated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_refund.refund
     *
     * @return the value of wechatpay_refund.refund
     *
     * @mbg.generated
     */
    public Integer getRefund() {
        return refund;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_refund.refund
     *
     * @param refund the value for wechatpay_refund.refund
     *
     * @mbg.generated
     */
    public void setRefund(Integer refund) {
        this.refund = refund;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_refund.payer_total
     *
     * @return the value of wechatpay_refund.payer_total
     *
     * @mbg.generated
     */
    public Integer getPayerTotal() {
        return payerTotal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_refund.payer_total
     *
     * @param payerTotal the value for wechatpay_refund.payer_total
     *
     * @mbg.generated
     */
    public void setPayerTotal(Integer payerTotal) {
        this.payerTotal = payerTotal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_refund.success_time
     *
     * @return the value of wechatpay_refund.success_time
     *
     * @mbg.generated
     */
    public LocalDateTime getSuccessTime() {
        return successTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_refund.success_time
     *
     * @param successTime the value for wechatpay_refund.success_time
     *
     * @mbg.generated
     */
    public void setSuccessTime(LocalDateTime successTime) {
        this.successTime = successTime;
    }
}
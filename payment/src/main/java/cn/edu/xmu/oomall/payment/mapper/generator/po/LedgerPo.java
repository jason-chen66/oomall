package cn.edu.xmu.oomall.payment.mapper.generator.po;

import java.time.LocalDateTime;

public class LedgerPo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.out_no
     *
     * @mbg.generated
     */
    private String outNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.trans_no
     *
     * @mbg.generated
     */
    private String transNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.amount
     *
     * @mbg.generated
     */
    private Long amount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.status
     *
     * @mbg.generated
     */
    private Byte status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.success_time
     *
     * @mbg.generated
     */
    private LocalDateTime successTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.type
     *
     * @mbg.generated
     */
    private Byte type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.adjust_id
     *
     * @mbg.generated
     */
    private Long adjustId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.adjust_name
     *
     * @mbg.generated
     */
    private String adjustName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.adjust_time
     *
     * @mbg.generated
     */
    private LocalDateTime adjustTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.shop_channel_id
     *
     * @mbg.generated
     */
    private Long shopChannelId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.trans_id
     *
     * @mbg.generated
     */
    private Long transId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.check_time
     *
     * @mbg.generated
     */
    private LocalDateTime checkTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.creator_id
     *
     * @mbg.generated
     */
    private Long creatorId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.creator_name
     *
     * @mbg.generated
     */
    private String creatorName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.modifier_id
     *
     * @mbg.generated
     */
    private Long modifierId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.modifier_name
     *
     * @mbg.generated
     */
    private String modifierName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.gmt_create
     *
     * @mbg.generated
     */
    private LocalDateTime gmtCreate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.gmt_modified
     *
     * @mbg.generated
     */
    private LocalDateTime gmtModified;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column payment_ledger.channel_id
     *
     * @mbg.generated
     */
    private Long channelId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.id
     *
     * @return the value of payment_ledger.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.id
     *
     * @param id the value for payment_ledger.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.out_no
     *
     * @return the value of payment_ledger.out_no
     *
     * @mbg.generated
     */
    public String getOutNo() {
        return outNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.out_no
     *
     * @param outNo the value for payment_ledger.out_no
     *
     * @mbg.generated
     */
    public void setOutNo(String outNo) {
        this.outNo = outNo == null ? null : outNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.trans_no
     *
     * @return the value of payment_ledger.trans_no
     *
     * @mbg.generated
     */
    public String getTransNo() {
        return transNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.trans_no
     *
     * @param transNo the value for payment_ledger.trans_no
     *
     * @mbg.generated
     */
    public void setTransNo(String transNo) {
        this.transNo = transNo == null ? null : transNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.amount
     *
     * @return the value of payment_ledger.amount
     *
     * @mbg.generated
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.amount
     *
     * @param amount the value for payment_ledger.amount
     *
     * @mbg.generated
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.status
     *
     * @return the value of payment_ledger.status
     *
     * @mbg.generated
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.status
     *
     * @param status the value for payment_ledger.status
     *
     * @mbg.generated
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.success_time
     *
     * @return the value of payment_ledger.success_time
     *
     * @mbg.generated
     */
    public LocalDateTime getSuccessTime() {
        return successTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.success_time
     *
     * @param successTime the value for payment_ledger.success_time
     *
     * @mbg.generated
     */
    public void setSuccessTime(LocalDateTime successTime) {
        this.successTime = successTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.type
     *
     * @return the value of payment_ledger.type
     *
     * @mbg.generated
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.type
     *
     * @param type the value for payment_ledger.type
     *
     * @mbg.generated
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.adjust_id
     *
     * @return the value of payment_ledger.adjust_id
     *
     * @mbg.generated
     */
    public Long getAdjustId() {
        return adjustId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.adjust_id
     *
     * @param adjustId the value for payment_ledger.adjust_id
     *
     * @mbg.generated
     */
    public void setAdjustId(Long adjustId) {
        this.adjustId = adjustId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.adjust_name
     *
     * @return the value of payment_ledger.adjust_name
     *
     * @mbg.generated
     */
    public String getAdjustName() {
        return adjustName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.adjust_name
     *
     * @param adjustName the value for payment_ledger.adjust_name
     *
     * @mbg.generated
     */
    public void setAdjustName(String adjustName) {
        this.adjustName = adjustName == null ? null : adjustName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.adjust_time
     *
     * @return the value of payment_ledger.adjust_time
     *
     * @mbg.generated
     */
    public LocalDateTime getAdjustTime() {
        return adjustTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.adjust_time
     *
     * @param adjustTime the value for payment_ledger.adjust_time
     *
     * @mbg.generated
     */
    public void setAdjustTime(LocalDateTime adjustTime) {
        this.adjustTime = adjustTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.shop_channel_id
     *
     * @return the value of payment_ledger.shop_channel_id
     *
     * @mbg.generated
     */
    public Long getShopChannelId() {
        return shopChannelId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.shop_channel_id
     *
     * @param shopChannelId the value for payment_ledger.shop_channel_id
     *
     * @mbg.generated
     */
    public void setShopChannelId(Long shopChannelId) {
        this.shopChannelId = shopChannelId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.trans_id
     *
     * @return the value of payment_ledger.trans_id
     *
     * @mbg.generated
     */
    public Long getTransId() {
        return transId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.trans_id
     *
     * @param transId the value for payment_ledger.trans_id
     *
     * @mbg.generated
     */
    public void setTransId(Long transId) {
        this.transId = transId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.check_time
     *
     * @return the value of payment_ledger.check_time
     *
     * @mbg.generated
     */
    public LocalDateTime getCheckTime() {
        return checkTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.check_time
     *
     * @param checkTime the value for payment_ledger.check_time
     *
     * @mbg.generated
     */
    public void setCheckTime(LocalDateTime checkTime) {
        this.checkTime = checkTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.creator_id
     *
     * @return the value of payment_ledger.creator_id
     *
     * @mbg.generated
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.creator_id
     *
     * @param creatorId the value for payment_ledger.creator_id
     *
     * @mbg.generated
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.creator_name
     *
     * @return the value of payment_ledger.creator_name
     *
     * @mbg.generated
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.creator_name
     *
     * @param creatorName the value for payment_ledger.creator_name
     *
     * @mbg.generated
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName == null ? null : creatorName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.modifier_id
     *
     * @return the value of payment_ledger.modifier_id
     *
     * @mbg.generated
     */
    public Long getModifierId() {
        return modifierId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.modifier_id
     *
     * @param modifierId the value for payment_ledger.modifier_id
     *
     * @mbg.generated
     */
    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.modifier_name
     *
     * @return the value of payment_ledger.modifier_name
     *
     * @mbg.generated
     */
    public String getModifierName() {
        return modifierName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.modifier_name
     *
     * @param modifierName the value for payment_ledger.modifier_name
     *
     * @mbg.generated
     */
    public void setModifierName(String modifierName) {
        this.modifierName = modifierName == null ? null : modifierName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.gmt_create
     *
     * @return the value of payment_ledger.gmt_create
     *
     * @mbg.generated
     */
    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.gmt_create
     *
     * @param gmtCreate the value for payment_ledger.gmt_create
     *
     * @mbg.generated
     */
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.gmt_modified
     *
     * @return the value of payment_ledger.gmt_modified
     *
     * @mbg.generated
     */
    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.gmt_modified
     *
     * @param gmtModified the value for payment_ledger.gmt_modified
     *
     * @mbg.generated
     */
    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column payment_ledger.channel_id
     *
     * @return the value of payment_ledger.channel_id
     *
     * @mbg.generated
     */
    public Long getChannelId() {
        return channelId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column payment_ledger.channel_id
     *
     * @param channelId the value for payment_ledger.channel_id
     *
     * @mbg.generated
     */
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
}
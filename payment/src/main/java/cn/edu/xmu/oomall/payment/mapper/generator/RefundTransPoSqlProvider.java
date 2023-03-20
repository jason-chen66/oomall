package cn.edu.xmu.oomall.payment.mapper.generator;

import cn.edu.xmu.oomall.payment.mapper.generator.po.RefundTransPo;
import cn.edu.xmu.oomall.payment.mapper.generator.po.RefundTransPoExample.Criteria;
import cn.edu.xmu.oomall.payment.mapper.generator.po.RefundTransPoExample.Criterion;
import cn.edu.xmu.oomall.payment.mapper.generator.po.RefundTransPoExample;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class RefundTransPoSqlProvider {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_refund_trans
     *
     * @mbg.generated
     */
    public String insertSelective(RefundTransPo row) {
        SQL sql = new SQL();
        sql.INSERT_INTO("payment_refund_trans");
        
        if (row.getOutNo() != null) {
            sql.VALUES("`out_no`", "#{outNo,jdbcType=VARCHAR}");
        }
        
        if (row.getTransNo() != null) {
            sql.VALUES("`trans_no`", "#{transNo,jdbcType=VARCHAR}");
        }
        
        if (row.getAmount() != null) {
            sql.VALUES("`amount`", "#{amount,jdbcType=BIGINT}");
        }
        
        if (row.getStatus() != null) {
            sql.VALUES("`status`", "#{status,jdbcType=TINYINT}");
        }
        
        if (row.getSuccessTime() != null) {
            sql.VALUES("`success_time`", "#{successTime,jdbcType=TIMESTAMP}");
        }
        
        if (row.getAdjustId() != null) {
            sql.VALUES("`adjust_id`", "#{adjustId,jdbcType=BIGINT}");
        }
        
        if (row.getAdjustName() != null) {
            sql.VALUES("`adjust_name`", "#{adjustName,jdbcType=VARCHAR}");
        }
        
        if (row.getAdjustTime() != null) {
            sql.VALUES("`adjust_time`", "#{adjustTime,jdbcType=TIMESTAMP}");
        }
        
        if (row.getUserReceivedAccount() != null) {
            sql.VALUES("`user_received_account`", "#{userReceivedAccount,jdbcType=VARCHAR}");
        }
        
        if (row.getPayTransId() != null) {
            sql.VALUES("`pay_trans_id`", "#{payTransId,jdbcType=BIGINT}");
        }
        
        if (row.getShopChannelId() != null) {
            sql.VALUES("`shop_channel_id`", "#{shopChannelId,jdbcType=BIGINT}");
        }
        
        if (row.getCreatorId() != null) {
            sql.VALUES("`creator_id`", "#{creatorId,jdbcType=BIGINT}");
        }
        
        if (row.getCreatorName() != null) {
            sql.VALUES("`creator_name`", "#{creatorName,jdbcType=VARCHAR}");
        }
        
        if (row.getModifierId() != null) {
            sql.VALUES("`modifier_id`", "#{modifierId,jdbcType=BIGINT}");
        }
        
        if (row.getModifierName() != null) {
            sql.VALUES("`modifier_name`", "#{modifierName,jdbcType=VARCHAR}");
        }
        
        if (row.getGmtCreate() != null) {
            sql.VALUES("`gmt_create`", "#{gmtCreate,jdbcType=TIMESTAMP}");
        }
        
        if (row.getGmtModified() != null) {
            sql.VALUES("`gmt_modified`", "#{gmtModified,jdbcType=TIMESTAMP}");
        }
        
        if (row.getDivAmount() != null) {
            sql.VALUES("`div_amount`", "#{divAmount,jdbcType=BIGINT}");
        }
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_refund_trans
     *
     * @mbg.generated
     */
    public String selectByExample(RefundTransPoExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("`id`");
        } else {
            sql.SELECT("`id`");
        }
        sql.SELECT("`out_no`");
        sql.SELECT("`trans_no`");
        sql.SELECT("`amount`");
        sql.SELECT("`status`");
        sql.SELECT("`success_time`");
        sql.SELECT("`adjust_id`");
        sql.SELECT("`adjust_name`");
        sql.SELECT("`adjust_time`");
        sql.SELECT("`user_received_account`");
        sql.SELECT("`pay_trans_id`");
        sql.SELECT("`shop_channel_id`");
        sql.SELECT("`creator_id`");
        sql.SELECT("`creator_name`");
        sql.SELECT("`modifier_id`");
        sql.SELECT("`modifier_name`");
        sql.SELECT("`gmt_create`");
        sql.SELECT("`gmt_modified`");
        sql.SELECT("`div_amount`");
        sql.FROM("payment_refund_trans");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_refund_trans
     *
     * @mbg.generated
     */
    public String updateByExampleSelective(Map<String, Object> parameter) {
        RefundTransPo row = (RefundTransPo) parameter.get("row");
        RefundTransPoExample example = (RefundTransPoExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("payment_refund_trans");
        
        if (row.getId() != null) {
            sql.SET("`id` = #{row.id,jdbcType=BIGINT}");
        }
        
        if (row.getOutNo() != null) {
            sql.SET("`out_no` = #{row.outNo,jdbcType=VARCHAR}");
        }
        
        if (row.getTransNo() != null) {
            sql.SET("`trans_no` = #{row.transNo,jdbcType=VARCHAR}");
        }
        
        if (row.getAmount() != null) {
            sql.SET("`amount` = #{row.amount,jdbcType=BIGINT}");
        }
        
        if (row.getStatus() != null) {
            sql.SET("`status` = #{row.status,jdbcType=TINYINT}");
        }
        
        if (row.getSuccessTime() != null) {
            sql.SET("`success_time` = #{row.successTime,jdbcType=TIMESTAMP}");
        }
        
        if (row.getAdjustId() != null) {
            sql.SET("`adjust_id` = #{row.adjustId,jdbcType=BIGINT}");
        }
        
        if (row.getAdjustName() != null) {
            sql.SET("`adjust_name` = #{row.adjustName,jdbcType=VARCHAR}");
        }
        
        if (row.getAdjustTime() != null) {
            sql.SET("`adjust_time` = #{row.adjustTime,jdbcType=TIMESTAMP}");
        }
        
        if (row.getUserReceivedAccount() != null) {
            sql.SET("`user_received_account` = #{row.userReceivedAccount,jdbcType=VARCHAR}");
        }
        
        if (row.getPayTransId() != null) {
            sql.SET("`pay_trans_id` = #{row.payTransId,jdbcType=BIGINT}");
        }
        
        if (row.getShopChannelId() != null) {
            sql.SET("`shop_channel_id` = #{row.shopChannelId,jdbcType=BIGINT}");
        }
        
        if (row.getCreatorId() != null) {
            sql.SET("`creator_id` = #{row.creatorId,jdbcType=BIGINT}");
        }
        
        if (row.getCreatorName() != null) {
            sql.SET("`creator_name` = #{row.creatorName,jdbcType=VARCHAR}");
        }
        
        if (row.getModifierId() != null) {
            sql.SET("`modifier_id` = #{row.modifierId,jdbcType=BIGINT}");
        }
        
        if (row.getModifierName() != null) {
            sql.SET("`modifier_name` = #{row.modifierName,jdbcType=VARCHAR}");
        }
        
        if (row.getGmtCreate() != null) {
            sql.SET("`gmt_create` = #{row.gmtCreate,jdbcType=TIMESTAMP}");
        }
        
        if (row.getGmtModified() != null) {
            sql.SET("`gmt_modified` = #{row.gmtModified,jdbcType=TIMESTAMP}");
        }
        
        if (row.getDivAmount() != null) {
            sql.SET("`div_amount` = #{row.divAmount,jdbcType=BIGINT}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_refund_trans
     *
     * @mbg.generated
     */
    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("payment_refund_trans");
        
        sql.SET("`id` = #{row.id,jdbcType=BIGINT}");
        sql.SET("`out_no` = #{row.outNo,jdbcType=VARCHAR}");
        sql.SET("`trans_no` = #{row.transNo,jdbcType=VARCHAR}");
        sql.SET("`amount` = #{row.amount,jdbcType=BIGINT}");
        sql.SET("`status` = #{row.status,jdbcType=TINYINT}");
        sql.SET("`success_time` = #{row.successTime,jdbcType=TIMESTAMP}");
        sql.SET("`adjust_id` = #{row.adjustId,jdbcType=BIGINT}");
        sql.SET("`adjust_name` = #{row.adjustName,jdbcType=VARCHAR}");
        sql.SET("`adjust_time` = #{row.adjustTime,jdbcType=TIMESTAMP}");
        sql.SET("`user_received_account` = #{row.userReceivedAccount,jdbcType=VARCHAR}");
        sql.SET("`pay_trans_id` = #{row.payTransId,jdbcType=BIGINT}");
        sql.SET("`shop_channel_id` = #{row.shopChannelId,jdbcType=BIGINT}");
        sql.SET("`creator_id` = #{row.creatorId,jdbcType=BIGINT}");
        sql.SET("`creator_name` = #{row.creatorName,jdbcType=VARCHAR}");
        sql.SET("`modifier_id` = #{row.modifierId,jdbcType=BIGINT}");
        sql.SET("`modifier_name` = #{row.modifierName,jdbcType=VARCHAR}");
        sql.SET("`gmt_create` = #{row.gmtCreate,jdbcType=TIMESTAMP}");
        sql.SET("`gmt_modified` = #{row.gmtModified,jdbcType=TIMESTAMP}");
        sql.SET("`div_amount` = #{row.divAmount,jdbcType=BIGINT}");
        
        RefundTransPoExample example = (RefundTransPoExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_refund_trans
     *
     * @mbg.generated
     */
    public String updateByPrimaryKeySelective(RefundTransPo row) {
        SQL sql = new SQL();
        sql.UPDATE("payment_refund_trans");
        
        if (row.getOutNo() != null) {
            sql.SET("`out_no` = #{outNo,jdbcType=VARCHAR}");
        }
        
        if (row.getTransNo() != null) {
            sql.SET("`trans_no` = #{transNo,jdbcType=VARCHAR}");
        }
        
        if (row.getAmount() != null) {
            sql.SET("`amount` = #{amount,jdbcType=BIGINT}");
        }
        
        if (row.getStatus() != null) {
            sql.SET("`status` = #{status,jdbcType=TINYINT}");
        }
        
        if (row.getSuccessTime() != null) {
            sql.SET("`success_time` = #{successTime,jdbcType=TIMESTAMP}");
        }
        
        if (row.getAdjustId() != null) {
            sql.SET("`adjust_id` = #{adjustId,jdbcType=BIGINT}");
        }
        
        if (row.getAdjustName() != null) {
            sql.SET("`adjust_name` = #{adjustName,jdbcType=VARCHAR}");
        }
        
        if (row.getAdjustTime() != null) {
            sql.SET("`adjust_time` = #{adjustTime,jdbcType=TIMESTAMP}");
        }
        
        if (row.getUserReceivedAccount() != null) {
            sql.SET("`user_received_account` = #{userReceivedAccount,jdbcType=VARCHAR}");
        }
        
        if (row.getPayTransId() != null) {
            sql.SET("`pay_trans_id` = #{payTransId,jdbcType=BIGINT}");
        }
        
        if (row.getShopChannelId() != null) {
            sql.SET("`shop_channel_id` = #{shopChannelId,jdbcType=BIGINT}");
        }
        
        if (row.getCreatorId() != null) {
            sql.SET("`creator_id` = #{creatorId,jdbcType=BIGINT}");
        }
        
        if (row.getCreatorName() != null) {
            sql.SET("`creator_name` = #{creatorName,jdbcType=VARCHAR}");
        }
        
        if (row.getModifierId() != null) {
            sql.SET("`modifier_id` = #{modifierId,jdbcType=BIGINT}");
        }
        
        if (row.getModifierName() != null) {
            sql.SET("`modifier_name` = #{modifierName,jdbcType=VARCHAR}");
        }
        
        if (row.getGmtCreate() != null) {
            sql.SET("`gmt_create` = #{gmtCreate,jdbcType=TIMESTAMP}");
        }
        
        if (row.getGmtModified() != null) {
            sql.SET("`gmt_modified` = #{gmtModified,jdbcType=TIMESTAMP}");
        }
        
        if (row.getDivAmount() != null) {
            sql.SET("`div_amount` = #{divAmount,jdbcType=BIGINT}");
        }
        
        sql.WHERE("`id` = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_refund_trans
     *
     * @mbg.generated
     */
    protected void applyWhere(SQL sql, RefundTransPoExample example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        
        if (sb.length() > 0) {
            sql.WHERE(sb.toString());
        }
    }
}
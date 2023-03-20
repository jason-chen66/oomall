package cn.edu.xmu.oomall.freight.mapper.generator;

import cn.edu.xmu.oomall.freight.mapper.generator.po.ShopLogisticsPo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.ShopLogisticsPoExample.Criteria;
import cn.edu.xmu.oomall.freight.mapper.generator.po.ShopLogisticsPoExample.Criterion;
import cn.edu.xmu.oomall.freight.mapper.generator.po.ShopLogisticsPoExample;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class ShopLogisticsPoSqlProvider {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_shop_logistics
     *
     * @mbg.generated
     */
    public String insertSelective(ShopLogisticsPo row) {
        SQL sql = new SQL();
        sql.INSERT_INTO("freight_shop_logistics");
        
        if (row.getShopId() != null) {
            sql.VALUES("`shop_id`", "#{shopId,jdbcType=BIGINT}");
        }
        
        if (row.getLogisticsId() != null) {
            sql.VALUES("`logistics_id`", "#{logisticsId,jdbcType=BIGINT}");
        }
        
        if (row.getSecret() != null) {
            sql.VALUES("`secret`", "#{secret,jdbcType=VARCHAR}");
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
        
        if (row.getInvalid() != null) {
            sql.VALUES("`invalid`", "#{invalid,jdbcType=TINYINT}");
        }
        
        if (row.getPriority() != null) {
            sql.VALUES("`priority`", "#{priority,jdbcType=INTEGER}");
        }
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_shop_logistics
     *
     * @mbg.generated
     */
    public String selectByExample(ShopLogisticsPoExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("`id`");
        } else {
            sql.SELECT("`id`");
        }
        sql.SELECT("`shop_id`");
        sql.SELECT("`logistics_id`");
        sql.SELECT("`secret`");
        sql.SELECT("`creator_id`");
        sql.SELECT("`creator_name`");
        sql.SELECT("`modifier_id`");
        sql.SELECT("`modifier_name`");
        sql.SELECT("`gmt_create`");
        sql.SELECT("`gmt_modified`");
        sql.SELECT("`invalid`");
        sql.SELECT("`priority`");
        sql.FROM("freight_shop_logistics");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_shop_logistics
     *
     * @mbg.generated
     */
    public String updateByExampleSelective(Map<String, Object> parameter) {
        ShopLogisticsPo row = (ShopLogisticsPo) parameter.get("row");
        ShopLogisticsPoExample example = (ShopLogisticsPoExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("freight_shop_logistics");
        
        if (row.getId() != null) {
            sql.SET("`id` = #{row.id,jdbcType=BIGINT}");
        }
        
        if (row.getShopId() != null) {
            sql.SET("`shop_id` = #{row.shopId,jdbcType=BIGINT}");
        }
        
        if (row.getLogisticsId() != null) {
            sql.SET("`logistics_id` = #{row.logisticsId,jdbcType=BIGINT}");
        }
        
        if (row.getSecret() != null) {
            sql.SET("`secret` = #{row.secret,jdbcType=VARCHAR}");
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
        
        if (row.getInvalid() != null) {
            sql.SET("`invalid` = #{row.invalid,jdbcType=TINYINT}");
        }
        
        if (row.getPriority() != null) {
            sql.SET("`priority` = #{row.priority,jdbcType=INTEGER}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_shop_logistics
     *
     * @mbg.generated
     */
    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("freight_shop_logistics");
        
        sql.SET("`id` = #{row.id,jdbcType=BIGINT}");
        sql.SET("`shop_id` = #{row.shopId,jdbcType=BIGINT}");
        sql.SET("`logistics_id` = #{row.logisticsId,jdbcType=BIGINT}");
        sql.SET("`secret` = #{row.secret,jdbcType=VARCHAR}");
        sql.SET("`creator_id` = #{row.creatorId,jdbcType=BIGINT}");
        sql.SET("`creator_name` = #{row.creatorName,jdbcType=VARCHAR}");
        sql.SET("`modifier_id` = #{row.modifierId,jdbcType=BIGINT}");
        sql.SET("`modifier_name` = #{row.modifierName,jdbcType=VARCHAR}");
        sql.SET("`gmt_create` = #{row.gmtCreate,jdbcType=TIMESTAMP}");
        sql.SET("`gmt_modified` = #{row.gmtModified,jdbcType=TIMESTAMP}");
        sql.SET("`invalid` = #{row.invalid,jdbcType=TINYINT}");
        sql.SET("`priority` = #{row.priority,jdbcType=INTEGER}");
        
        ShopLogisticsPoExample example = (ShopLogisticsPoExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_shop_logistics
     *
     * @mbg.generated
     */
    public String updateByPrimaryKeySelective(ShopLogisticsPo row) {
        SQL sql = new SQL();
        sql.UPDATE("freight_shop_logistics");
        
        if (row.getShopId() != null) {
            sql.SET("`shop_id` = #{shopId,jdbcType=BIGINT}");
        }
        
        if (row.getLogisticsId() != null) {
            sql.SET("`logistics_id` = #{logisticsId,jdbcType=BIGINT}");
        }
        
        if (row.getSecret() != null) {
            sql.SET("`secret` = #{secret,jdbcType=VARCHAR}");
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
        
        if (row.getInvalid() != null) {
            sql.SET("`invalid` = #{invalid,jdbcType=TINYINT}");
        }
        
        if (row.getPriority() != null) {
            sql.SET("`priority` = #{priority,jdbcType=INTEGER}");
        }
        
        sql.WHERE("`id` = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_shop_logistics
     *
     * @mbg.generated
     */
    protected void applyWhere(SQL sql, ShopLogisticsPoExample example, boolean includeExamplePhrase) {
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
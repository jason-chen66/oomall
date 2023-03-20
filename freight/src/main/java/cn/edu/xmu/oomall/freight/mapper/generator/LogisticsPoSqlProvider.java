package cn.edu.xmu.oomall.freight.mapper.generator;

import cn.edu.xmu.oomall.freight.mapper.generator.po.LogisticsPo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.LogisticsPoExample.Criteria;
import cn.edu.xmu.oomall.freight.mapper.generator.po.LogisticsPoExample.Criterion;
import cn.edu.xmu.oomall.freight.mapper.generator.po.LogisticsPoExample;
import java.util.List;
import org.apache.ibatis.jdbc.SQL;

public class LogisticsPoSqlProvider {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_logistics
     *
     * @mbg.generated
     */
    public String insertSelective(LogisticsPo row) {
        SQL sql = new SQL();
        sql.INSERT_INTO("freight_logistics");
        
        if (row.getName() != null) {
            sql.VALUES("`name`", "#{name,jdbcType=VARCHAR}");
        }
        
        if (row.getAppId() != null) {
            sql.VALUES("`app_id`", "#{appId,jdbcType=VARCHAR}");
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
        
        if (row.getSnPattern() != null) {
            sql.VALUES("`sn_pattern`", "#{snPattern,jdbcType=VARCHAR}");
        }
        
        if (row.getSecret() != null) {
            sql.VALUES("`secret`", "#{secret,jdbcType=VARCHAR}");
        }
        
        if (row.getLogisticsClass() != null) {
            sql.VALUES("`logistics_class`", "#{logisticsClass,jdbcType=VARCHAR}");
        }
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_logistics
     *
     * @mbg.generated
     */
    public String selectByExample(LogisticsPoExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("`id`");
        } else {
            sql.SELECT("`id`");
        }
        sql.SELECT("`name`");
        sql.SELECT("`app_id`");
        sql.SELECT("`creator_id`");
        sql.SELECT("`creator_name`");
        sql.SELECT("`modifier_id`");
        sql.SELECT("`modifier_name`");
        sql.SELECT("`gmt_create`");
        sql.SELECT("`gmt_modified`");
        sql.SELECT("`sn_pattern`");
        sql.SELECT("`secret`");
        sql.SELECT("`logistics_class`");
        sql.FROM("freight_logistics");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_logistics
     *
     * @mbg.generated
     */
    public String updateByPrimaryKeySelective(LogisticsPo row) {
        SQL sql = new SQL();
        sql.UPDATE("freight_logistics");
        
        if (row.getName() != null) {
            sql.SET("`name` = #{name,jdbcType=VARCHAR}");
        }
        
        if (row.getAppId() != null) {
            sql.SET("`app_id` = #{appId,jdbcType=VARCHAR}");
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
        
        if (row.getSnPattern() != null) {
            sql.SET("`sn_pattern` = #{snPattern,jdbcType=VARCHAR}");
        }
        
        if (row.getSecret() != null) {
            sql.SET("`secret` = #{secret,jdbcType=VARCHAR}");
        }
        
        if (row.getLogisticsClass() != null) {
            sql.SET("`logistics_class` = #{logisticsClass,jdbcType=VARCHAR}");
        }
        
        sql.WHERE("`id` = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_logistics
     *
     * @mbg.generated
     */
    protected void applyWhere(SQL sql, LogisticsPoExample example, boolean includeExamplePhrase) {
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
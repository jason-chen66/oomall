package cn.edu.xmu.oomall.freight.mapper.generator;

import cn.edu.xmu.oomall.freight.mapper.generator.po.LogisticsPo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.LogisticsPoExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface LogisticsPoMapper {

    @Select({
            "select * ",
            "from freight_logistics"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="app_id", property="appId", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
            @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
            @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
            @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="sn_pattern", property="snPattern", jdbcType=JdbcType.VARCHAR),
            @Result(column="secret", property="secret", jdbcType=JdbcType.VARCHAR)
    })
    List<LogisticsPo> selectIdAndNameAndPattern();
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_logistics
     *
     * @mbg.generated
     */
    @Delete({
        "delete from freight_logistics",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_logistics
     *
     * @mbg.generated
     */
    @Insert({
        "insert into freight_logistics (`name`, `app_id`, ",
        "`creator_id`, `creator_name`, ",
        "`modifier_id`, `modifier_name`, ",
        "`gmt_create`, `gmt_modified`, ",
        "`sn_pattern`, `secret`, ",
        "`logistics_class`)",
        "values (#{name,jdbcType=VARCHAR}, #{appId,jdbcType=VARCHAR}, ",
        "#{creatorId,jdbcType=BIGINT}, #{creatorName,jdbcType=VARCHAR}, ",
        "#{modifierId,jdbcType=BIGINT}, #{modifierName,jdbcType=VARCHAR}, ",
        "#{gmtCreate,jdbcType=TIMESTAMP}, #{gmtModified,jdbcType=TIMESTAMP}, ",
        "#{snPattern,jdbcType=VARCHAR}, #{secret,jdbcType=VARCHAR}, ",
        "#{logisticsClass,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(LogisticsPo row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_logistics
     *
     * @mbg.generated
     */
    @InsertProvider(type=LogisticsPoSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(LogisticsPo row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_logistics
     *
     * @mbg.generated
     */
    @SelectProvider(type=LogisticsPoSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="app_id", property="appId", jdbcType=JdbcType.VARCHAR),
        @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
        @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
        @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
        @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
        @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="sn_pattern", property="snPattern", jdbcType=JdbcType.VARCHAR),
        @Result(column="secret", property="secret", jdbcType=JdbcType.VARCHAR),
        @Result(column="logistics_class", property="logisticsClass", jdbcType=JdbcType.VARCHAR)
    })
    List<LogisticsPo> selectByExample(LogisticsPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_logistics
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "`id`, `name`, `app_id`, `creator_id`, `creator_name`, `modifier_id`, `modifier_name`, ",
        "`gmt_create`, `gmt_modified`, `sn_pattern`, `secret`, `logistics_class`",
        "from freight_logistics",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="app_id", property="appId", jdbcType=JdbcType.VARCHAR),
        @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
        @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
        @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
        @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
        @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="sn_pattern", property="snPattern", jdbcType=JdbcType.VARCHAR),
        @Result(column="secret", property="secret", jdbcType=JdbcType.VARCHAR),
        @Result(column="logistics_class", property="logisticsClass", jdbcType=JdbcType.VARCHAR)
    })
    LogisticsPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_logistics
     *
     * @mbg.generated
     */
    @UpdateProvider(type=LogisticsPoSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(LogisticsPo row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table freight_logistics
     *
     * @mbg.generated
     */
    @Update({
        "update freight_logistics",
        "set `name` = #{name,jdbcType=VARCHAR},",
          "`app_id` = #{appId,jdbcType=VARCHAR},",
          "`creator_id` = #{creatorId,jdbcType=BIGINT},",
          "`creator_name` = #{creatorName,jdbcType=VARCHAR},",
          "`modifier_id` = #{modifierId,jdbcType=BIGINT},",
          "`modifier_name` = #{modifierName,jdbcType=VARCHAR},",
          "`gmt_create` = #{gmtCreate,jdbcType=TIMESTAMP},",
          "`gmt_modified` = #{gmtModified,jdbcType=TIMESTAMP},",
          "`sn_pattern` = #{snPattern,jdbcType=VARCHAR},",
          "`secret` = #{secret,jdbcType=VARCHAR},",
          "`logistics_class` = #{logisticsClass,jdbcType=VARCHAR}",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(LogisticsPo row);
}
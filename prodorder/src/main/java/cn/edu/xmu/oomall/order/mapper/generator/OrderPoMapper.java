package cn.edu.xmu.oomall.order.mapper.generator;

import cn.edu.xmu.oomall.order.mapper.generator.po.OrderPo;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderPoExample;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

@Mapper
public interface OrderPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_order
     *
     * @mbg.generated
     */
    @Delete({
        "delete from order_order",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_order
     *
     * @mbg.generated
     */
    @Insert({
        "insert into order_order (`customer_id`, `shop_id`, ",
        "`order_sn`, `pid`, `consignee`, ",
        "`region_id`, `address`, ",
        "`mobile`, `message`, ",
        "`package_id`, `express_fee`, ",
        "`discount_price`, `origin_price`, ",
        "`point`, `status`, `creator_id`, ",
        "`creator_name`, `modifier_id`, ",
        "`modifier_name`, `gmt_create`, ",
        "`gmt_modified`)",
        "values (#{customerId,jdbcType=BIGINT}, #{shopId,jdbcType=BIGINT}, ",
        "#{orderSn,jdbcType=VARCHAR}, #{pid,jdbcType=BIGINT}, #{consignee,jdbcType=VARCHAR}, ",
        "#{regionId,jdbcType=BIGINT}, #{address,jdbcType=VARCHAR}, ",
        "#{mobile,jdbcType=VARCHAR}, #{message,jdbcType=VARCHAR}, ",
        "#{packageId,jdbcType=BIGINT}, #{expressFee,jdbcType=BIGINT}, ",
        "#{discountPrice,jdbcType=BIGINT}, #{originPrice,jdbcType=BIGINT}, ",
        "#{point,jdbcType=BIGINT}, #{status,jdbcType=INTEGER}, #{creatorId,jdbcType=BIGINT}, ",
        "#{creatorName,jdbcType=VARCHAR}, #{modifierId,jdbcType=BIGINT}, ",
        "#{modifierName,jdbcType=VARCHAR}, #{gmtCreate,jdbcType=TIMESTAMP}, ",
        "#{gmtModified,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(OrderPo row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_order
     *
     * @mbg.generated
     */
    @InsertProvider(type=OrderPoSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(OrderPo row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_order
     *
     * @mbg.generated
     */
    @SelectProvider(type=OrderPoSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="customer_id", property="customerId", jdbcType=JdbcType.BIGINT),
        @Result(column="shop_id", property="shopId", jdbcType=JdbcType.BIGINT),
        @Result(column="order_sn", property="orderSn", jdbcType=JdbcType.VARCHAR),
        @Result(column="pid", property="pid", jdbcType=JdbcType.BIGINT),
        @Result(column="consignee", property="consignee", jdbcType=JdbcType.VARCHAR),
        @Result(column="region_id", property="regionId", jdbcType=JdbcType.BIGINT),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
        @Result(column="mobile", property="mobile", jdbcType=JdbcType.VARCHAR),
        @Result(column="message", property="message", jdbcType=JdbcType.VARCHAR),
        @Result(column="package_id", property="packageId", jdbcType=JdbcType.BIGINT),
        @Result(column="express_fee", property="expressFee", jdbcType=JdbcType.BIGINT),
        @Result(column="discount_price", property="discountPrice", jdbcType=JdbcType.BIGINT),
        @Result(column="origin_price", property="originPrice", jdbcType=JdbcType.BIGINT),
        @Result(column="point", property="point", jdbcType=JdbcType.BIGINT),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
        @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
        @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
        @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
        @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP)
    })
    List<OrderPo> selectByExample(OrderPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_order
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "`id`, `customer_id`, `shop_id`, `order_sn`, `pid`, `consignee`, `region_id`, ",
        "`address`, `mobile`, `message`, `package_id`, `express_fee`, `discount_price`, ",
        "`origin_price`, `point`, `status`, `creator_id`, `creator_name`, `modifier_id`, ",
        "`modifier_name`, `gmt_create`, `gmt_modified`",
        "from order_order",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="customer_id", property="customerId", jdbcType=JdbcType.BIGINT),
        @Result(column="shop_id", property="shopId", jdbcType=JdbcType.BIGINT),
        @Result(column="order_sn", property="orderSn", jdbcType=JdbcType.VARCHAR),
        @Result(column="pid", property="pid", jdbcType=JdbcType.BIGINT),
        @Result(column="consignee", property="consignee", jdbcType=JdbcType.VARCHAR),
        @Result(column="region_id", property="regionId", jdbcType=JdbcType.BIGINT),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
        @Result(column="mobile", property="mobile", jdbcType=JdbcType.VARCHAR),
        @Result(column="message", property="message", jdbcType=JdbcType.VARCHAR),
        @Result(column="package_id", property="packageId", jdbcType=JdbcType.BIGINT),
        @Result(column="express_fee", property="expressFee", jdbcType=JdbcType.BIGINT),
        @Result(column="discount_price", property="discountPrice", jdbcType=JdbcType.BIGINT),
        @Result(column="origin_price", property="originPrice", jdbcType=JdbcType.BIGINT),
        @Result(column="point", property="point", jdbcType=JdbcType.BIGINT),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
        @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
        @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
        @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
        @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP)
    })
    OrderPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_order
     *
     * @mbg.generated
     */
    @UpdateProvider(type=OrderPoSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("row") OrderPo row, @Param("example") OrderPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_order
     *
     * @mbg.generated
     */
    @UpdateProvider(type=OrderPoSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("row") OrderPo row, @Param("example") OrderPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_order
     *
     * @mbg.generated
     */
    @UpdateProvider(type=OrderPoSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(OrderPo row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_order
     *
     * @mbg.generated
     */
    @Update({
        "update order_order",
        "set `customer_id` = #{customerId,jdbcType=BIGINT},",
          "`shop_id` = #{shopId,jdbcType=BIGINT},",
          "`order_sn` = #{orderSn,jdbcType=VARCHAR},",
          "`pid` = #{pid,jdbcType=BIGINT},",
          "`consignee` = #{consignee,jdbcType=VARCHAR},",
          "`region_id` = #{regionId,jdbcType=BIGINT},",
          "`address` = #{address,jdbcType=VARCHAR},",
          "`mobile` = #{mobile,jdbcType=VARCHAR},",
          "`message` = #{message,jdbcType=VARCHAR},",
          "`package_id` = #{packageId,jdbcType=BIGINT},",
          "`express_fee` = #{expressFee,jdbcType=BIGINT},",
          "`discount_price` = #{discountPrice,jdbcType=BIGINT},",
          "`origin_price` = #{originPrice,jdbcType=BIGINT},",
          "`point` = #{point,jdbcType=BIGINT},",
          "`status` = #{status,jdbcType=INTEGER},",
          "`creator_id` = #{creatorId,jdbcType=BIGINT},",
          "`creator_name` = #{creatorName,jdbcType=VARCHAR},",
          "`modifier_id` = #{modifierId,jdbcType=BIGINT},",
          "`modifier_name` = #{modifierName,jdbcType=VARCHAR},",
          "`gmt_create` = #{gmtCreate,jdbcType=TIMESTAMP},",
          "`gmt_modified` = #{gmtModified,jdbcType=TIMESTAMP}",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(OrderPo row);
}
package cn.edu.xmu.oomall.freight.dao.openfeign.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * 平台地区明细信息
 */
public class Region implements Serializable {

    /**
     * 有效
     */
    public static Byte VALID = 0;

    /**
     * 暂停
     */
    public static Byte SUSPEND = 1;

    /**
     * 废弃
     */
    public static Byte INVALID = 2;

    /**
     * 地区id
     */
    @Getter
    @Setter
    private Long id;

    // private Long pid;

    /**
     * 名称name
     */
    @Getter
    @Setter
    private String name;

    /**
     * 状态 0有效/1暂停/2废弃
     */
    @Getter
    @Setter
    private Byte status;

    /**
     * 级别
     */
    @Getter
    @Setter
    private Long level;

    /**
     * 简称
     */
    @Getter
    @Setter
    private String shortName;

    /**
     * 全称
     */
    @Getter
    @Setter
    private String mergerName;

    /**
     * 拼音
     */
    @Getter
    @Setter
    private String pinyin;

    /**
     * 经度
     */
    @Getter
    @Setter
    private String lng;

    /**
     * 纬度
     */
    @Getter
    @Setter
    private String lat;

    /**
     * 地区码
     */
    @Getter
    @Setter
    private String areaCode;

    /**
     * 邮政编码
     */
    @Getter
    @Setter
    private String zipCode;

    /**
     * 电话区号
     */
    @Getter
    @Setter
    private String cityCode;

    @Getter
    @Setter
    private IdNameDto creator;

    @Getter
    @Setter
    private LocalDateTime gmtCreate;

    @Getter
    @Setter
    private IdNameDto modifier;

    @Getter
    @Setter
    private LocalDateTime gmtModified;
}

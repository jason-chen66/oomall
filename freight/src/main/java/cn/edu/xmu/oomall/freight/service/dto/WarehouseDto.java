package cn.edu.xmu.oomall.freight.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WarehouseDto {
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 所在地区
     */
    private SimpleRegionDto region;

    /**
     * 联系人
     */
    private String senderName;

    /**
     * 联系电话
     */
    private String senderMobile;

    /**
     * 状态 0 有效 1无效
     */
    private Byte invalid;

    /**
     * 优先级
     */
    private Integer priority;

    private SimpleAdminUserDto creator;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private SimpleAdminUserDto modifier;
}

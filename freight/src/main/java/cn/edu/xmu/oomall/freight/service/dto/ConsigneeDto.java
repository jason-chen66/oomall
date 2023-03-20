package cn.edu.xmu.oomall.freight.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsigneeDto {
    /**
     * 联系人姓名
     */
    private String name;

    /**
     * 联系人电话
     */
    private String mobile;

    /**
     * 地区号
     */
    private Long regionId;

    /**
     * 详细地址
     */
    private String address;
}

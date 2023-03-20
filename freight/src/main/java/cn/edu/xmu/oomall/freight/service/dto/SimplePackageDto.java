package cn.edu.xmu.oomall.freight.service.dto;

import lombok.Data;

@Data
public class SimplePackageDto {

    private Long id;
    /**
     * 运单号
     */
    private String billCode;
}

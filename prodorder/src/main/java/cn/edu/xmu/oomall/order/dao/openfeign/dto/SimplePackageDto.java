package cn.edu.xmu.oomall.order.dao.openfeign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimplePackageDto {
    /**
     * packageId
     */
    private Long id;
    /**
     * billCode
     */
    private String billCode;
}

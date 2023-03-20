package cn.edu.xmu.oomall.freight.dao.openfeign.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleRegion {
    private Long id;

    /**
     * 名称
     */
    private String name;
}

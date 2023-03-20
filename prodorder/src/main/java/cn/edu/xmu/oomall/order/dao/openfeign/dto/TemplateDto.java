package cn.edu.xmu.oomall.order.dao.openfeign.dto;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TemplateDto extends OOMallObject {

    /**
     * 运费模板id
     */
    private Long id;

    /**
     * 卖家定义的运费模板名
     */
    private String name;

    /**
     * 默认模板
     */
    private Integer defaultModel;


}

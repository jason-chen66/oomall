//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * 平台物流渠道
 */
public class Logistics extends OOMallObject implements Serializable{

    /**
     * 渠道id
     */
    private Long id;

     /**
     * 渠道名称
     */
    private String name;

    /**
     * 平台应用id
     */
    private String appId;


    /**
     * 运单编号匹配规则
     */
    private String snPattern;

    /**
     * 密钥
     */
    private String secret;

    /**
     * 物流类
     */
    private String logisticsClass;

    @Builder
    public Logistics (Long id,String name,String appId,String secret,String snPattern,String logisticClass)
    {
        this.id=id;
        this.secret=secret;
        this.name=name;
        this.appId=appId;
        this.snPattern=snPattern;
        this.logisticsClass=logisticClass;
    }


}

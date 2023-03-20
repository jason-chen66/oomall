package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.dao.LogisticsDao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 商铺物流
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopLogistics extends OOMallObject implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ShopLogistics.class);

    /**
     * 有效
     */
    public static Byte VALID = 0;
    /**
     * 无效
     */
    public static Byte INVALID = 1;


    /**
     * 商铺id
     */
    @Getter
    @Setter
    private Long shopId;

    /**
     * 物流id
     */
    @Getter
    @Setter
    private Long logisticsId;

    /**
     * 状态
     */
    @Getter
    @Setter
    private Byte invalid;

    /**
     * 密钥
     */
    @Getter
    @Setter
    private String secret;

    /**
     * 优先级
     */
    @Getter
    @Setter
    private int priority;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    LogisticsDao logisticsDao;

    Logistics logistics;

    public Logistics getLogistics() throws BusinessException {
        if (null == this.logistics && null != this.logisticsDao) {
            logger.debug("getChannel: this.channelId = {}", this.logisticsId);
            this.logistics = this.logisticsDao.findById(this.logisticsId);
        }
        return this.logistics;
    }


    @Builder
    public ShopLogistics(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified, Long shopId, Long logisticsId, Byte invalid, String secret, int priority, LogisticsDao logisticsDao, Logistics logistics) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.shopId = shopId;
        this.logisticsId = logisticsId;
        this.invalid = invalid;
        this.secret = secret;
        this.priority = priority;
        this.logisticsDao = logisticsDao;
        this.logistics = logistics;
    }

    public ShopLogistics(Long shopId, Long logisticsId, Byte invalid, String secret, int priority) {
        this.shopId = shopId;
        this.logisticsId = logisticsId;
        this.invalid = invalid;
        this.secret = secret;
        this.priority = priority;
    }
}


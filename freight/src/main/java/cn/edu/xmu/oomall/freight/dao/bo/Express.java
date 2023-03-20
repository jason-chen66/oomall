package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.Region;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Express extends OOMallObject implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Express.class);

    /**
     * 未发货
     */
    public static final Byte UNSENT = 0;
    /**
     * 在途
     */
    public static final Byte ON = 1;
    /**
     * 签收
     */
    public static final Byte SIGNED = 2;
    /**
     * 取消
     */
    public static final Byte CANCEL = 3;
    /**
     * 拒收
     */
    public static final Byte REFUSED = 4;
    /**
     * 已退回
     */
    public static final Byte RETURNED = 5;
    /**
     *  丢失
     */
    public static final Byte LOST = 6;
    /**
     * 回收
     */
    public static final Byte RECYCLED = 7;
    /**
     * 破损
     */
    public static final Byte BROKEN = 8;

    /**
     * 状态
     * 默认未发货
     */
        @Getter
        @Setter
    private Byte status = Express.UNSENT;

    /**
     * 状态和名称的对应
     */
    public static final Map<Byte, String> STATUSNAMES = new HashMap(){
        {
            put(UNSENT, "未发货");
            put(ON, "在途");
            put(SIGNED, "已签收");
            put(CANCEL, "取消");
            put(REFUSED, "拒收");
            put(RETURNED, "已退回");
            put(LOST, "丢失");
            put(RECYCLED,"回收");
            put(BROKEN, "破损");
        }
    };

    /**
     * 允许的状态迁移
     */
    private static final Map<Byte, Set<Byte>> toStatus = new HashMap<>(){
        {
            put(UNSENT, new HashSet<>(){
                {
                    add(ON);
                    add(CANCEL);
                }
            });
            put(ON, new HashSet<>(){
                {
                    add(REFUSED);
                    add(SIGNED);
                    add(LOST);
                }
            });
            put(REFUSED, new HashSet<>(){
                {
                    add(LOST);
                    add(RETURNED);
                }
            });
            put(RETURNED, new HashSet<>(){
                {
                    add(RECYCLED);
                    add(BROKEN);
                }
            });
        }
    };

    /**
     * 是否允许状态迁移
     */
    public boolean allowStatus(Byte status){
        boolean ret = false;

        if (null != status && null != this.status){
            Set<Byte> allowStatusSet = toStatus.get(this.status);
            if (null != allowStatusSet) {
                ret = allowStatusSet.contains(status);
            }
        }
        return ret;
    }

    /**
     * 获得当前状态名称
     */
    public String getStatusName(){
        return STATUSNAMES.get(this.status);
    }

    /**
     * 商铺物流
     */
    @Getter
    @Setter
    private  Long shopLogisticsId;
    @JsonIgnore
    private ShopLogistics shopLogistics;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private ShopLogisticsDao shopLogisticsDao;

    public ShopLogistics getShopLogistics(){
        if (null == shopLogistics && null != shopLogisticsDao){
            shopLogistics = shopLogisticsDao.findById(this.shopLogisticsId);
        }
        return shopLogistics;
    }


    /**
     * 商铺id
     */
    @Getter
    @Setter
    private Long shopId;

    /**
     * 运单号
     */
    @Getter
    @Setter
    private String billCode;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private RegionDao regionDao;
    /**
     * 发货地
     */
    @Getter
    @Setter
    private Long senderRegionId;
    @JsonIgnore
    private Region senderRegion;

    public Region getSenderRegion(){
        if (null == senderRegion && null != regionDao){
            senderRegion = regionDao.getRegionById(this.senderRegionId).getData();
        }
        return senderRegion;
    }

    /**
     * 收货地
     */
    @Getter
    @Setter
    private Long deliveryRegionId;
    @JsonIgnore
    private Region deliveryRegion;

    public Region getDeliveryRegion(){
        if (null == deliveryRegion && null != regionDao){
            deliveryRegion = regionDao.getRegionById(this.deliveryRegionId).getData();
        }
        return deliveryRegion;
    }

    /**
     * 发货详细地址
     */
    @Getter
    @Setter
    private String senderAddress;

    /**
     * 收获地详细地址
     */
    @Getter
    @Setter
    private String deliveryAddress;

    /**
     * 发货人姓名
     */
    @Getter
    @Setter
    private String senderName;

    /**
     * 发货人电话
     */
    @Getter
    @Setter
    private String senderMobile;

    /**
     * 收货人姓名
     */
    @Getter
    @Setter
    private String deliveryName;

    /**
     * 收货人电话
     */
    @Getter
    @Setter
    private String deliveryMobile;

    @Builder
    public Express(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified, Byte status, Long shopLogisticsId, ShopLogistics shopLogistics, ShopLogisticsDao shopLogisticsDao, Long shopId, String billCode, RegionDao regionDao, Long senderRegionId, Region senderRegion, Long deliveryRegionId, Region deliveryRegion, String senderAddress, String deliveryAddress, String senderName, String senderMobile, String deliveryName, String deliveryMobile) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.status = status;
        this.shopLogisticsId = shopLogisticsId;
        this.shopLogistics = shopLogistics;
        this.shopLogisticsDao = shopLogisticsDao;
        this.shopId = shopId;
        this.billCode = billCode;
        this.regionDao = regionDao;
        this.senderRegionId = senderRegionId;
        this.senderRegion = senderRegion;
        this.deliveryRegionId = deliveryRegionId;
        this.deliveryRegion = deliveryRegion;
        this.senderAddress = senderAddress;
        this.deliveryAddress = deliveryAddress;
        this.senderName = senderName;
        this.senderMobile = senderMobile;
        this.deliveryName = deliveryName;
        this.deliveryMobile = deliveryMobile;
    }

}

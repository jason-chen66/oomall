package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.Region;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * 商铺物流不可达地区
 */
public class Undeliverable extends OOMallObject implements Serializable {


    /**
     * 地区id
     */
    @Getter
    @Setter
    private Long regionId;

    /**
     * 店铺物流id
     */
    @Getter
    @Setter
    private Long shopLogisticsId;

    /**
     * 开始时间
     */
    @Getter
    @Setter
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @Getter
    @Setter
    private LocalDateTime endTime;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private RegionDao regionDao;

    private Region region;

    public Region getRegion(){
        if(null == region && null != this.regionDao){
            this.region = this.regionDao.getRegionById(this.regionId).getData();
        }
        return this.region;
    }

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private ShopLogisticsDao shopLogisticsDao;

    private ShopLogistics shopLogistics;

    public ShopLogistics getShopLogistics(){
        if(null == shopLogistics && null != this.shopLogisticsDao){
            this.shopLogistics = this.shopLogisticsDao.findById(this.shopLogisticsId);
        }
        return this.shopLogistics;
    }
    @Builder
    public Undeliverable(Long id,Long regionId,Long shopLogisticsId,LocalDateTime beginTime,LocalDateTime endTime)
    {
        this.id=id;
        this.regionId=regionId;
        this.shopLogisticsId=shopLogisticsId;
        this.beginTime=beginTime;
        this.endTime=endTime;
    }



}

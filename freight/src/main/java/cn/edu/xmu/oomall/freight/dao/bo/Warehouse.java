package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.dao.WarehouseLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.WarehouseRegionDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.Region;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * 仓库
 */
public class Warehouse extends OOMallObject implements Serializable {

    public static final Byte VALID =0;
    public static final Byte INVALID =1;

    /**
     * 名称
     */
    @Getter
    @Setter
    private String name;

    /**
     * 详细地址
     */
    @Getter
    @Setter
    private String address;

    /**
     * 所在地区id
     */
    @Getter
    @Setter
    private Long regionId;

    /**
     * 联系人
     */
    @Getter
    @Setter
    private String senderName;

    /**
     * 联系电话
     */
    @Getter
    @Setter
    private String senderMobile;

    /**
     * 店铺id
     */
    @Getter
    @Setter
    private Long shopId;

    /**
     * 优先级
     */
    @Getter
    @Setter
    private Integer priority;

    /**
     * 状态 0 有效 1无效 2删除
     */
    @Getter
    @Setter
    private Byte invalid;

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
    private WarehouseLogisticsDao warehouseLogisticsDao;

    private List<WarehouseLogistics> warehouseLogisticsList;

    public List<WarehouseLogistics> getWarehouseLogisticsList(){
        if(null == this.warehouseLogisticsList && null != this.warehouseLogisticsDao){
            this.warehouseLogisticsList = this.warehouseLogisticsDao.retrieveByWarehouseIdInternal(this.id);
        }
        return this.warehouseLogisticsList;
    }

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private WarehouseRegionDao warehouseRegionDao;

    /**
     * 获取仓库地区,因为仓库地区太多了,所以没有在Warehouse对象中没有WarehouseRegionList字段
     * @return
     */
    public List<WarehouseRegion> getWarehouseRegionList(){
        if(null != this.warehouseRegionDao){
            return this.warehouseRegionDao.retrieveByWarehouseIdInternal(this.id);
        }
        return null;
    }

    @Builder
    public Warehouse(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified, String name, String address, Long regionId, String senderName, String senderMobile, Long shopId, Integer priority, Byte invalid, RegionDao regionDao, Region region, WarehouseLogisticsDao warehouseLogisticsDao, List<WarehouseLogistics> warehouseLogisticsList, WarehouseRegionDao warehouseRegionDao) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.name = name;
        this.address = address;
        this.regionId = regionId;
        this.senderName = senderName;
        this.senderMobile = senderMobile;
        this.shopId = shopId;
        this.priority = priority;
        this.invalid = invalid;
        this.regionDao = regionDao;
        this.region = region;
        this.warehouseLogisticsDao = warehouseLogisticsDao;
        this.warehouseLogisticsList = warehouseLogisticsList;
        this.warehouseRegionDao = warehouseRegionDao;
    }

    public Warehouse(String name, String address, Long regionId, String senderName, String senderMobile, Long shopId) {
        this.name=name;
        this.address=address;
        this.regionId=regionId;
        this.senderName=senderName;
        this.senderMobile=senderMobile;
        this.shopId=shopId;
        this.invalid=Warehouse.VALID;
        this.priority=1000;
    }


}


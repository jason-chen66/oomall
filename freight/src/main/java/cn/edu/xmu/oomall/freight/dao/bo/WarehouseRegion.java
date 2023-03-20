package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.dao.WarehouseDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.Region;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * 仓库发货地区
 */
public class WarehouseRegion extends OOMallObject implements Serializable {

    /**
     * 地区id
     */
    @Getter
    @Setter
    private Long regionId;

    /**
     * 仓库id
     */
    @Getter
    @Setter
    private Long warehouseId;

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
    private WarehouseDao warehouseDao;

    private Warehouse warehouse;

    public Warehouse getWarehouse() {
        if (null == warehouse && null != warehouseDao) {
            this.warehouse = this.warehouseDao.findById(warehouseId);
        }
        return this.warehouse;
    }

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

    @Builder
    public WarehouseRegion(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified, Long regionId, Long warehouseId, LocalDateTime beginTime, LocalDateTime endTime, WarehouseDao warehouseDao, Warehouse warehouse, RegionDao regionDao, Region region) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.regionId = regionId;
        this.warehouseId = warehouseId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.warehouseDao = warehouseDao;
        this.warehouse = warehouse;
        this.regionDao = regionDao;
        this.region = region;
    }

}

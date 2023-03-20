package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.WarehouseDao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * 仓库物流
 */
public class WarehouseLogistics extends OOMallObject implements Serializable {

    public static final Byte VALID =0;

    public static final Byte INVALID =1;

    /**
     * 仓库id
     */
    @Getter
    @Setter
    private Long warehouseId;

    /**
     * 物流id
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

    /**
     * 状态
     */
    @Getter
    @Setter
    private byte invalid;

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
    private ShopLogisticsDao shopLogisticsDao;

    private ShopLogistics shopLogistics;

    public ShopLogistics getShopLogistics() {
        if (null == shopLogistics && null != shopLogisticsDao) {
            this.shopLogistics = this.shopLogisticsDao.findById(shopLogisticsId);
        }
        return this.shopLogistics;
    }

    @Builder
    public WarehouseLogistics(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified, Long warehouseId, Long shopLogisticsId, LocalDateTime beginTime, LocalDateTime endTime, byte invalid, WarehouseDao warehouseDao, Warehouse warehouse, ShopLogisticsDao shopLogisticsDao, ShopLogistics shopLogistics) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.warehouseId = warehouseId;
        this.shopLogisticsId = shopLogisticsId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.invalid = invalid;
        this.warehouseDao = warehouseDao;
        this.warehouse = warehouse;
        this.shopLogisticsDao = shopLogisticsDao;
        this.shopLogistics = shopLogistics;
    }

    public WarehouseLogistics(Long warehouseId, Long shopLogisticsId, LocalDateTime beginTime, LocalDateTime endTime) {
        this.warehouseId = warehouseId;
        this.shopLogisticsId = shopLogisticsId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.invalid = WarehouseLogistics.VALID;
    }
}

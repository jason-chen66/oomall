package cn.edu.xmu.oomall.freight.dao.bo.handler;

import cn.edu.xmu.oomall.freight.dao.WarehouseDao;
import cn.edu.xmu.oomall.freight.dao.WarehouseRegionDao;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WarehouseRegionHandler extends DeliveryHandler{

    private WarehouseRegionDao warehouseRegionDao;

    @Autowired
    public WarehouseRegionHandler(WarehouseRegionDao warehouseRegionDao, WarehouseDao warehouseDao) {
        this.warehouseRegionDao = warehouseRegionDao;
    }

    @Override
    public DeliveryInfo doHandler(DeliveryInfo deliveryInfo) {
        //通过配送地区获得可用仓库列表(排好序)
        List<WarehouseRegion> warehouseRegionList =
                warehouseRegionDao.retrieveAllByRegionIdInternal(deliveryInfo.shopId,deliveryInfo.getDeliveryRegionId()).stream()
                //过滤掉过期的仓库配送地区
                .filter(warehouseRegion -> warehouseRegion.getEndTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        deliveryInfo.setWarehouseRegionList(warehouseRegionList);

        if (null != next) {
            return next.doHandler(deliveryInfo);
        }
        return deliveryInfo;
    }
}

package cn.edu.xmu.oomall.freight.dao.bo.handler;

import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WarehouseHandler extends DeliveryHandler{

    @Override
    public DeliveryInfo doHandler(DeliveryInfo deliveryInfo) {
        //查找所有仓库
        List<Warehouse> warehouseList = deliveryInfo.getWarehouseRegionList().stream()
                //获取配送仓库列表
                .map(WarehouseRegion::getWarehouse)
                //过滤掉无效仓库
                .filter(warehouse ->warehouse.getInvalid() == Warehouse.VALID)
                //按优先级对仓库排序
                .sorted(Comparator.comparing(Warehouse::getPriority))
                .collect(Collectors.toList());

        deliveryInfo.setAvailableWarehouseList(warehouseList);
        if (null != next) {
            return next.doHandler(deliveryInfo);
        }
        return deliveryInfo;
    }
}


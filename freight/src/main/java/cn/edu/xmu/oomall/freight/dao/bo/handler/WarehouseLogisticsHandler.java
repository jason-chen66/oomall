package cn.edu.xmu.oomall.freight.dao.bo.handler;

import cn.edu.xmu.oomall.freight.dao.WarehouseLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseLogistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;

@Component
public class WarehouseLogisticsHandler extends DeliveryHandler{

    private WarehouseLogisticsDao warehouseLogisticsDao;

    @Autowired
    public WarehouseLogisticsHandler(WarehouseLogisticsDao warehouseLogisticsDao) {
        this.warehouseLogisticsDao = warehouseLogisticsDao;
    }

    @Override
    public DeliveryInfo doHandler(DeliveryInfo deliveryInfo) {
        OutterLoop:for(Warehouse warehouse : deliveryInfo.availableWarehouseList){
            for(ShopLogistics shopLogistics : deliveryInfo.availableShopLogisticsList){
                //判断是否存在该仓库配送物流
                WarehouseLogistics warehouseLogistics = warehouseLogisticsDao.findByWarehouseIdAndShopLogisticsId(warehouse.getId(), shopLogistics.getId());
                if (warehouseLogistics != null) {
                    //判断该仓库物流是否有效
                    if(warehouseLogistics.getInvalid() == WarehouseLogistics.VALID){
                        //判断该仓库物流是否过期
                        if(warehouseLogistics.getEndTime().isAfter(LocalDateTime.now())){
                            //获得所需的配送仓库和配送物流
                            deliveryInfo.setWarehouse(warehouse);
                            deliveryInfo.setShopLogistics(shopLogistics);
                            //跳出所有循环
                            break OutterLoop;
                        }
                    }
                }
            }
        }

        if (null != next) {
            return next.doHandler(deliveryInfo);
        }
        return deliveryInfo;
    }
}

package cn.edu.xmu.oomall.freight.dao.bo.handler;

import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShopLogisticsHandler extends DeliveryHandler{
    private ShopLogisticsDao shopLogisticsDao;

    @Autowired
    public ShopLogisticsHandler(ShopLogisticsDao shopLogisticsDao) {
        this.shopLogisticsDao = shopLogisticsDao;
    }

    @Override
    public DeliveryInfo doHandler(DeliveryInfo deliveryInfo) {
        //查找店铺所有物流
        List<ShopLogistics> shopLogisticsList= shopLogisticsDao.retrieveByShopIdInternal(deliveryInfo.getShopId()).stream()
                //过滤无效店铺物流
                .filter(shopLogistics->shopLogistics.getInvalid()==ShopLogistics.VALID)
                //店铺物流按优先级排序
                .sorted(Comparator.comparingInt(ShopLogistics::getPriority))
                .collect(Collectors.toList());

        deliveryInfo.setShopLogisticsList(shopLogisticsList);
        if (null != next) {
            return next.doHandler(deliveryInfo);
        }
        return deliveryInfo;
    }
}


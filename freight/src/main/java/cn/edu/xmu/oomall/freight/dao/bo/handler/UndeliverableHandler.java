package cn.edu.xmu.oomall.freight.dao.bo.handler;

import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.UndeliverableDao;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.dao.bo.Undeliverable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class UndeliverableHandler extends DeliveryHandler{

    private UndeliverableDao undeliverableDao;


    @Autowired
    public UndeliverableHandler(UndeliverableDao undeliverableDao, ShopLogisticsDao shopLogisticsDao) {
        this.undeliverableDao = undeliverableDao;
    }


    @Override
    public DeliveryInfo doHandler(DeliveryInfo deliveryInfo) {
        //查找店铺该地区不可达物流
        List<Long> undeliverableShopLogisticsIdList = undeliverableDao.retrieveByRegionIdInternal(deliveryInfo.getDeliveryRegionId()).stream()
                //过滤过期的不可达
                .filter(undeliverable->undeliverable.getEndTime().isAfter(LocalDateTime.now()))
                //获取不可达物流id列表
                .map(Undeliverable::getShopLogisticsId).collect(Collectors.toList());

        //获得可达店铺物流列表:
        List<ShopLogistics> availableShopLogisticsList = new ArrayList<>();
        deliveryInfo.getShopLogisticsList().stream()
                //过滤掉该地区不可达店铺物流
                .forEach(bo -> {
                    if (!undeliverableShopLogisticsIdList.contains(bo.getId())) {
                    availableShopLogisticsList.add(bo);
                }});

        //设置可用物流列表
        deliveryInfo.setAvailableShopLogisticsList(availableShopLogisticsList);
        if (null != next) {
           return next.doHandler(deliveryInfo);
        }
        return deliveryInfo;
    }
}

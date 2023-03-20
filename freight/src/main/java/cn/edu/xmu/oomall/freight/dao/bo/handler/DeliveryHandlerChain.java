package cn.edu.xmu.oomall.freight.dao.bo.handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Component
public class DeliveryHandlerChain {
    private DeliveryHandler chain;

    private ShopLogisticsHandler shopLogisticsHandler;

    private UndeliverableHandler undeliverableHandler;

    private WarehouseLogisticsHandler warehouseLogisticsHandler;

    private WarehouseRegionHandler warehouseRegionHandler;

    private WarehouseHandler warehouseHandler;


    @Autowired
    public DeliveryHandlerChain(ShopLogisticsHandler shopLogisticsHandler, UndeliverableHandler undeliverableHandler, WarehouseLogisticsHandler warehouseLogisticsHandler, WarehouseRegionHandler warehouseRegionHandler, WarehouseHandler warehouseHandler) {
        this.shopLogisticsHandler = shopLogisticsHandler;
        this.undeliverableHandler = undeliverableHandler;
        this.warehouseLogisticsHandler = warehouseLogisticsHandler;
        this.warehouseRegionHandler = warehouseRegionHandler;
        this.warehouseHandler = warehouseHandler;
    }

    @PostConstruct
    private void init() {
        chain =  new DeliveryHandlerChainBuilder()
                .addHandler(shopLogisticsHandler)
                .addHandler(undeliverableHandler)
                .addHandler(warehouseRegionHandler)
                .addHandler(warehouseHandler)
                .addHandler(warehouseLogisticsHandler)
                .build();
    }


    public DeliveryInfo doHandler(Long shopId, Long deliveryRegionId) {
        DeliveryInfo deliveryInfo = DeliveryInfo.builder().shopId(shopId).deliveryRegionId(deliveryRegionId).build();
        return chain.doHandler(deliveryInfo);
    }
}

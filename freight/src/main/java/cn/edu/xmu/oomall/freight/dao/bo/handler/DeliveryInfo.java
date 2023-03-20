package cn.edu.xmu.oomall.freight.dao.bo.handler;

import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DeliveryInfo {
    /*************************************************************************
     * 以下字段为提供给运单的结果: warehouse 和 shopLogistics
     *************************************************************************/

    Warehouse warehouse;

    ShopLogistics shopLogistics;


    /**************************************************************************
     * 以下是接收的参数
     *************************************************************************/

    /**
     * 运单送达地区ID
     */
    long deliveryRegionId;

    /**
     * 该运单的店铺
     */
    long shopId;

    /**************************************************************************
     * 以下是处理过程的中间参数
     *************************************************************************/

    /**
     * 商铺物流列表
     */
    List<ShopLogistics> shopLogisticsList;

    /**
     * 该运单可用商铺物流列表
     */
    List<ShopLogistics> availableShopLogisticsList;

    /**
     * 仓库地区列表
     */
    List<WarehouseRegion> warehouseRegionList;

    /**
     * 该运单可用仓库列表
     */
    List<Warehouse> availableWarehouseList;

}

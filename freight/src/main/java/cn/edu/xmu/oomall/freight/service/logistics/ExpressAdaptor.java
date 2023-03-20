package cn.edu.xmu.oomall.freight.service.logistics;

import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.service.logistics.dto.CancelExpressAdaptorDto;
import cn.edu.xmu.oomall.freight.service.logistics.dto.GetExpressAdaptorDto;
import cn.edu.xmu.oomall.freight.service.logistics.dto.CreateExpressAdaptorDto;
import cn.edu.xmu.oomall.freight.service.logistics.dto.GetRouteInfoAdaptorDto;


/**
 * 快递渠道适配器接口
 * 适配器模式
 */
public interface ExpressAdaptor {
    /**
     * 创建运单
     * @author 庄婉如
     * <p>
     * @param //Express,warehouselogistics,
     * @return PostExpressAdaptorDto
     */
    CreateExpressAdaptorDto createExpress(Express express);

    /**
     * 向第三方平台查询运单
     * <p>
     * @param packageId
     * @return
     */
    GetExpressAdaptorDto returnExpressByPackageId(String packageId);

    /**
     * 向第三方平台查询物流轨迹
     * @param billCode
     * @return
     */
    GetRouteInfoAdaptorDto returnRouteInfoByBillCode(String billCode);

    /**
     * 向第三方平台取消运单
     * @param cancelType
     * @param packageId
     * @return
     */
    CancelExpressAdaptorDto cancelExpressByPackageId(String cancelType,String packageId);
}

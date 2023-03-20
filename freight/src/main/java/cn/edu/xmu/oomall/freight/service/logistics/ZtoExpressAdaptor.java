package cn.edu.xmu.oomall.freight.service.logistics;

import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.Region;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.SimpleRegion;
import cn.edu.xmu.oomall.freight.service.logistics.dto.CancelExpressAdaptorDto;
import cn.edu.xmu.oomall.freight.service.logistics.dto.CreateExpressAdaptorDto;
import cn.edu.xmu.oomall.freight.service.logistics.dto.GetExpressAdaptorDto;
import cn.edu.xmu.oomall.freight.service.logistics.dto.GetRouteInfoAdaptorDto;
import cn.edu.xmu.oomall.freight.service.openfeign.ZtoExpressService;
import cn.edu.xmu.oomall.freight.service.openfeign.ZtoParam.*;
import cn.edu.xmu.oomall.freight.service.openfeign.util.TimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("ztoDao")
public class ZtoExpressAdaptor implements ExpressAdaptor{
    private Logger logger = LoggerFactory.getLogger(ZtoExpressAdaptor.class);

    private ZtoExpressService ztoExpressService;

    private RegionDao regionDao;

    @Autowired
    public ZtoExpressAdaptor(ZtoExpressService ztoExpressService, RegionDao regionDao) {
        this.ztoExpressService = ztoExpressService;
        this.regionDao = regionDao;
    }
    /**
     * 创建运单
     *
     * @return PostExpressAdaptorDto
     * @author 庄婉如
     * <p>
     */
    @Override
    public CreateExpressAdaptorDto createExpress(Express express) {
        // 获取发货地父子地区
        Region senderArea = express.getSenderRegion();
        List<SimpleRegion> senderList = regionDao.getParentRegionsById(senderArea.getId()).getData();

        // 获取收货地父子地区
        Region deliveryArea = express.getDeliveryRegion();
        List<SimpleRegion> deliveryList = regionDao.getParentRegionsById(deliveryArea.getId()).getData();

        ZtoCreateExpressParam.Receiver receiver = new ZtoCreateExpressParam.Receiver();
        ZtoCreateExpressParam.Sender sender = new ZtoCreateExpressParam.Sender();
        sender.setSenderName(express.getSenderName());
        sender.setSenderMobile(express.getSenderMobile());
        sender.setSenderProvince(senderList.get(1).getName());
        sender.setSenderCity(senderList.get(0).getName());
        sender.setSenderDistrict(senderArea.getName());
        sender.setSenderAddress(express.getSenderAddress());

        receiver.setReceiverName(express.getDeliveryName());
        receiver.setReceiverMobile(express.getDeliveryMobile());
        receiver.setReceiverProvince(deliveryList.get(1).getName());
        receiver.setReceiverCity(deliveryList.get(0).getName());
        receiver.setReceiverDistrict(deliveryArea.getName());
        receiver.setReceiverAddress(express.getDeliveryAddress());

        /*set param*/
        ZtoCreateExpressParam param = new ZtoCreateExpressParam();
        param.setPartnerType("1");
        param.setOrderType("1");
        param.setPartnerOrderCode(String.valueOf(express.getId()));
        param.setSenderInfo(sender);
        param.setReceiverInfo(receiver);
        ZtoCreateExpressRetObj retObj = ztoExpressService.createExpress(param).getData();

        /*set ret*/
        CreateExpressAdaptorDto dto = new CreateExpressAdaptorDto();
        if(retObj.getStatus())
        {
            dto.setStatus((byte) 1);
            dto.setBillCode(retObj.getResult().getSignBillInfo().getBillCode());
        }
        else
            dto.setStatus((byte) 0);
        dto.setMsg(retObj.getMessage());

        return dto;
    }

    /**
     * 向第三方查询订单
     * @param packageId
     * @return
     */
    @Override
    public GetExpressAdaptorDto returnExpressByPackageId(String packageId){
        /*set param*/
        ZtoGetExpressParam param = new ZtoGetExpressParam();
        param.setType(1);
        param.setOrderCode(packageId);
        ZtoGetExpressRetObj retObj = ztoExpressService.getExpressByOrderId(param).getData();

        /*set ret*/
        GetExpressAdaptorDto dto = new GetExpressAdaptorDto();
        if(retObj.getStatus())
        {
            dto.setStatus((byte) 1);
            dto.setOrderId(Long.valueOf(retObj.getData().get(0).getOrderCode()));
            dto.setBillCode(retObj.getData().get(0).getBillCode());

            byte status;
            switch (retObj.getData().get(0).getOrderStatus())
            {
                case -2:
                    status = Express.CANCEL;
                    break;
                case 99:
                    status = Express.SIGNED;
                    break;
                case 0:
                    status = Express.UNSENT;
                    break;
                default:
                    status = Express.ON;
            }
            dto.setExpressStatus(status);
        }
        else
            dto.setStatus((byte) 0);
        dto.setMsg(retObj.getMessage());
        return dto;
    }


    /**
     * 向第三方平台查询物流轨迹
     * @param billCode
     * @return
     */
    @Override
    public GetRouteInfoAdaptorDto returnRouteInfoByBillCode(String billCode) {
        /*set param*/
        ZtoGetRouteParam param = new ZtoGetRouteParam();
        param.setBillCode(billCode);
        ZtoGetRouteRetObj retObj = ztoExpressService.getRoute(param).getData();

        /*set ret*/
        GetRouteInfoAdaptorDto dto = new GetRouteInfoAdaptorDto();
        if(retObj.getStatus())
        {
            dto.setStatus((byte) 1);
            List<GetRouteInfoAdaptorDto.Route> routes = new ArrayList<>();
            retObj.getResult().stream().forEach(item ->
                    routes.add(new GetRouteInfoAdaptorDto.Route(item.getDesc(), TimeFormatter.StrToLocalDateTime(item.getScanDate()))));
            dto.setRoutes(routes);
        }
        else
            dto.setStatus((byte) 0);
        dto.setMsg(retObj.getMessage());
        return dto;
    }


    /**
     * 向第三方平台取消运单
     * @param cancelType
     * @param packageId
     * @return
     */
    @Override
    public CancelExpressAdaptorDto cancelExpressByPackageId(String cancelType,String packageId) {
        /*set param*/
        ZtoCancelExpressParam param = new ZtoCancelExpressParam();
        param.setCancelType("1");
        param.setOrderCode(packageId);
        ZtoCancelExpressRetObj retObj = ztoExpressService.cancelExpress(param).getData();

        /*set ret*/
        CancelExpressAdaptorDto dto = new CancelExpressAdaptorDto();
        if(retObj.getStatus())
        {
            dto.setStatus((byte) 1);
        }
        else
            dto.setStatus((byte) 0);
        dto.setMsg(retObj.getMessage());
        return dto;
    }
}

package cn.edu.xmu.oomall.freight.service.logistics;

import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.Region;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.SimpleRegion;
import cn.edu.xmu.oomall.freight.service.logistics.dto.CancelExpressAdaptorDto;
import cn.edu.xmu.oomall.freight.service.logistics.dto.CreateExpressAdaptorDto;
import cn.edu.xmu.oomall.freight.service.logistics.dto.GetExpressAdaptorDto;
import cn.edu.xmu.oomall.freight.service.logistics.dto.GetRouteInfoAdaptorDto;
import cn.edu.xmu.oomall.freight.service.openfeign.JtExpressService;
import cn.edu.xmu.oomall.freight.service.openfeign.JtParam.*;
import cn.edu.xmu.oomall.freight.service.openfeign.util.TimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("jtDao")
public class JtExpressAdaptor implements ExpressAdaptor{
    private Logger logger = LoggerFactory.getLogger(JtExpressAdaptor.class);

    private JtExpressService jtExpressService;

    private RegionDao regionDao;

    @Autowired
    public JtExpressAdaptor(JtExpressService jtExpressService, RegionDao regionDao) {
        this.jtExpressService = jtExpressService;
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


        JtCreateExpressParam.Sender sender = new JtCreateExpressParam.Sender();
        JtCreateExpressParam.Receiver receiver = new JtCreateExpressParam.Receiver();
        sender.setName(express.getSenderName());
        sender.setMobile(express.getSenderMobile());
        sender.setProv(senderList.get(1).getName());
        sender.setCity(senderList.get(0).getName());
        sender.setArea(senderArea.getName());
        sender.setAddress(express.getSenderAddress());

        receiver.setName(express.getDeliveryName());
        receiver.setMobile(express.getDeliveryMobile());
        receiver.setProv(deliveryList.get(1).getName());
        receiver.setCity(deliveryList.get(0).getName());
        receiver.setArea(deliveryArea.getName());
        receiver.setAddress(express.getDeliveryAddress());

        /*set param*/
        JtCreateExpressParam param = new JtCreateExpressParam();
        param.setTxlogisticId(String.valueOf(express.getId()));
        param.setExpressType("EZ");
        param.setOrderType("2");
        param.setServerType("02");
        param.setDeliveryType("04");
        param.setPayType("PP_PM");
        param.setGoodsType("bm000006");
        param.setSender(sender);
        param.setReceiver(receiver);
        param.setWeight("0.02");

        JtCreateExpressRetObj retObj = jtExpressService.createExpress(param).getData();

        /*set ret*/
        CreateExpressAdaptorDto dto = new CreateExpressAdaptorDto();
        dto.setStatus(Byte.parseByte(retObj.getCode()));
        dto.setMsg(retObj.getMsg());
        if(dto.getStatus() == 1)
        {
            dto.setBillCode(retObj.getData().getBillCode());
        }
        return dto;
    }

    /**
     * 向第三方平台查询运单
     * <p>
     * @param packageId
     * @return GetExpressAdaptorDto
     */
    @Override
    public GetExpressAdaptorDto returnExpressByPackageId(String packageId)
    {
        List<String> orderIdList = new ArrayList<>();
        orderIdList.add(String.valueOf(packageId));

        /*set param*/
        JtGetExpressParam param = new JtGetExpressParam();
        param.setCommand(2);
        param.setSerialNumber(orderIdList);
        JtGetExpressRetObj retObj = jtExpressService.getExpressByOrderId(param).getData();

        /*set ret*/
        GetExpressAdaptorDto dto = new GetExpressAdaptorDto();
        dto.setStatus(Byte.parseByte(retObj.getCode()));
        dto.setMsg(retObj.getMsg());
        if(dto.getStatus() == 1)
        {
            dto.setOrderId(retObj.getData().get(0).getOrderNumber());
            dto.setBillCode(retObj.getData().get(0).getBillCode());
            byte status;
            switch (retObj.getData().get(0).getOrderStatus())
            {
                case "104":
                    status = Express.CANCEL;
                    break;
                case "103":
                    status = Express.SIGNED;
                    break;
                case "100":
                    status = Express.UNSENT;
                    break;
                default:
                    status = Express.ON;
            }
            dto.setExpressStatus(status);
        }
        return dto;
    }

    /**
     * 向第三方查询物流轨迹
     * @param billCode
     * @return
     */
    @Override
    public GetRouteInfoAdaptorDto returnRouteInfoByBillCode(String billCode) {
        /*set param*/
        JtGetRouteParam param = new JtGetRouteParam();
        param.setBillCodes(billCode);
        JtGetRouteRetObj retObj = jtExpressService.getRoute(param).getData();

        /*set ret*/
        GetRouteInfoAdaptorDto dto = new GetRouteInfoAdaptorDto();
        dto.setStatus(Byte.parseByte(retObj.getCode()));
        dto.setMsg(retObj.getMsg());
        if(dto.getStatus() == 1)
        {
            List<GetRouteInfoAdaptorDto.Route> routes = new ArrayList<>();
            retObj.getData().getDetails().stream().forEach(item ->{
                        routes.add(new GetRouteInfoAdaptorDto.Route(item.getDesc(),
                                TimeFormatter.StrToLocalDateTime(item.getScanTime())));
                    });
            dto.setRoutes(routes);
        }
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
        JtCancelExpressParam param = new JtCancelExpressParam();
        param.setTxlogisticId(String.valueOf(packageId));
        param.setOrderType("2");
        param.setReason(null);
        JtCancelExpressRetObj retObj = jtExpressService.cancelExpress(param).getData();

        /*set ret*/
        CancelExpressAdaptorDto dto = new CancelExpressAdaptorDto();
        dto.setStatus(Byte.parseByte(retObj.getCode()));
        dto.setMsg(retObj.getMsg());
        return dto;
    }
}

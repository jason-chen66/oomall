package cn.edu.xmu.oomall.freight.service.logistics;

import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.Region;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.SimpleRegion;
import cn.edu.xmu.oomall.freight.service.logistics.dto.CancelExpressAdaptorDto;
import cn.edu.xmu.oomall.freight.service.logistics.dto.CreateExpressAdaptorDto;
import cn.edu.xmu.oomall.freight.service.logistics.dto.GetExpressAdaptorDto;
import cn.edu.xmu.oomall.freight.service.logistics.dto.GetRouteInfoAdaptorDto;
import cn.edu.xmu.oomall.freight.service.openfeign.SfExpressService;
import cn.edu.xmu.oomall.freight.service.openfeign.SfParam.*;
import cn.edu.xmu.oomall.freight.service.openfeign.util.TimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@Service("sfDao")
public class SfExpressAdaptor implements ExpressAdaptor{
    private Logger logger = LoggerFactory.getLogger(SfExpressAdaptor.class);

    private SfExpressService sfExpressService;

    private RegionDao regionDao;


    @Autowired
    public SfExpressAdaptor(SfExpressService sfExpressService, RegionDao regionDao) {
        this.sfExpressService = sfExpressService;
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

        SfCreateExpressParam.ContactInfo sender =new SfCreateExpressParam.ContactInfo();
        SfCreateExpressParam.ContactInfo receiver =new SfCreateExpressParam.ContactInfo();
        sender.setContactType((byte) 1);
        sender.setContact(express.getSenderName());
        sender.setTel(express.getSenderMobile());
        sender.setProvince(senderList.get(1).getName());
        sender.setCity(senderList.get(0).getName());
        sender.setCounty(senderArea.getName());
        sender.setAddress(express.getSenderAddress());

        receiver.setContactType((byte) 2);
        receiver.setContact(express.getDeliveryName());
        receiver.setTel(express.getDeliveryMobile());
        receiver.setProvince(deliveryList.get(1).getName());
        receiver.setCity(deliveryList.get(0).getName());
        receiver.setCounty(deliveryArea.getName());
        receiver.setAddress(express.getDeliveryAddress());

        List<SfCreateExpressParam.ContactInfo> contactInfoList = new ArrayList<>() ;
        contactInfoList.add(sender);
        contactInfoList.add(receiver);

        /*set param*/
        SfCreateExpressParam param = new SfCreateExpressParam();
        param.setOrderId(String.valueOf(express.getId()));
        param.setCargoDetails(null);
        param.setContactInfoList(contactInfoList);
        param.setExpressTypeId(1);
        param.setIsReturnRoutelabel((byte) 0);
        SfCreateExpressRetObj retObj = sfExpressService.createExpress(param).getData();

        /*set ret*/
        CreateExpressAdaptorDto dto = new CreateExpressAdaptorDto();
        if(retObj.getSuccess() == "true")
        {
            dto.setStatus((byte)1);
            dto.setBillCode(retObj.getMsgData().getWaybillNoInfoList().get(0).getWaybillNo());
        }
        else
            dto.setStatus((byte)0);
        dto.setMsg(retObj.getErrorMsg());
        return dto;
    }


    /**
     * 向第三方查询订单
     * @param packageId
     * @return
     */
    @Override
    public GetExpressAdaptorDto returnExpressByPackageId(String packageId) {
        /*set param*/
        SfGetExpressParam param = new SfGetExpressParam();
        param.setOrderId(packageId);
        SfGetExpressRetObj retObj = sfExpressService.getExpressByOrderId(param).getData();

        /*set ret*/
        GetExpressAdaptorDto dto = new GetExpressAdaptorDto();
        if(retObj.getSuccess() == "true")
        {
            dto.setStatus((byte)1);
            /*顺丰的运单状态在路由信息那边处理*/
            dto.setExpressStatus((byte) -1);
        }
        else
            dto.setStatus((byte)0);
        dto.setMsg(retObj.getErrorMsg());
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
        SfGetRouteParam param = new SfGetRouteParam();
        param.setTrackingNum(1);
        List<String> trackingNum = new ArrayList<>();
        trackingNum.add(billCode);
        SfGetRouteRetObj retObj = sfExpressService.getRoute(param).getData();

        /*set ret*/
        GetRouteInfoAdaptorDto dto = new GetRouteInfoAdaptorDto();
        if(retObj.getSuccess() == "true")
        {
            dto.setStatus((byte) 1);
            List<SfRouteResultParam.Route> sfRouteList = retObj.getMsgData().getRouteResps().get(0).getRoutes();

            //用最新得路由信息来设置运单状态
            String curOpCode = sfRouteList.stream().max(Comparator.comparing(SfRouteResultParam.Route::getAcceptTime)).get().getOpcode();
            byte status;
            switch (curOpCode)
            {
                case "101":
                    status = Express.CANCEL;
                    break;
                case "80":
                    status = Express.SIGNED;
                    break;
                case "100":
                    status = Express.UNSENT;
                    break;
                default:
                    status = Express.ON;
            }
            dto.setExpressStatus(status);

            List<GetRouteInfoAdaptorDto.Route> routes = new ArrayList<>();
            sfRouteList.stream().forEach(
                    item -> routes.add(new GetRouteInfoAdaptorDto.Route(item.getRemark(),
                            TimeFormatter.StrToLocalDateTime(item.getAcceptTime().toString()))));
            dto.setRoutes(routes);
        }
        else
            dto.setStatus((byte)0);
        dto.setMsg(retObj.getErrorMsg());
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
        SfCancelExpressParam param = new SfCancelExpressParam();
        param.setOrderId(packageId);
        SfCancelExpressRetObj retObj = sfExpressService.cancelExpress(param).getData();

        /*set ret*/
        CancelExpressAdaptorDto dto = new CancelExpressAdaptorDto();
        if(retObj.getSuccess() == "true")
        {
            dto.setStatus((byte) 1);
        }
        else
            dto.setStatus((byte)0);
        dto.setMsg(retObj.getErrorMsg());
        return dto;
    }
}

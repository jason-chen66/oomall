package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.controller.vo.ConfirmExpressVo;
import cn.edu.xmu.oomall.freight.controller.vo.ExpressVo;
import cn.edu.xmu.oomall.freight.dao.ExpressDao;
import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.dao.bo.handler.DeliveryHandlerChain;
import cn.edu.xmu.oomall.freight.dao.bo.handler.DeliveryInfo;
import cn.edu.xmu.oomall.freight.service.dto.*;
import cn.edu.xmu.oomall.freight.service.logistics.ExpressAdaptor;
import cn.edu.xmu.oomall.freight.service.logistics.ExpressAdaptorFactory;
import cn.edu.xmu.oomall.freight.service.logistics.dto.CancelExpressAdaptorDto;
import cn.edu.xmu.oomall.freight.service.logistics.dto.CreateExpressAdaptorDto;
import cn.edu.xmu.oomall.freight.service.logistics.dto.GetExpressAdaptorDto;
import cn.edu.xmu.oomall.freight.service.logistics.dto.GetRouteInfoAdaptorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@Service
public class ExpressService {
    private static  final Logger logger = LoggerFactory.getLogger(LogisticsService.class);

    private static final int LostRecordTime = 1000000;

    private ExpressDao expressDao;

    private ShopLogisticsDao shopLogisticsDao;

    private DeliveryHandlerChain deliveryHandlerChain;

    private ExpressAdaptorFactory expressAdaptorFactory;


    @Autowired
    public ExpressService(ExpressDao expressDao, ShopLogisticsDao shopLogisticsDao, DeliveryHandlerChain deliveryHandlerChain, ExpressAdaptorFactory expressAdaptorFactory){//注入需要的Dao
        this.expressDao = expressDao;
        this.shopLogisticsDao = shopLogisticsDao;
        this.deliveryHandlerChain = deliveryHandlerChain;
        this.expressAdaptorFactory = expressAdaptorFactory;
    }

    /**
     * 创建运单
     * @param shopId
     * @param expressVo
     * @param user
     * @return
     */
    @Transactional
    public ReturnObject creatExpress(Long shopId, ExpressVo expressVo, UserDto user) throws BusinessException{
        if(expressVo.getShopLogisticId()!=0 && PLATFORM != shopId && shopLogisticsDao.findById(expressVo.getShopLogisticId()).getShopId() != shopId)
        {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "商铺", shopId));
        }

        // 如果shopLogistics为0，则通过责任链选择仓库
        if(expressVo.getShopLogisticId() == 0)
        {
            DeliveryInfo deliveryInfo = deliveryHandlerChain.doHandler(shopId,expressVo.getDelivery().getRegionId());
            ExpressVo.Consignee consignee = expressVo.new Consignee() ;
            if(null == expressVo.getSender())
            {
                consignee.setName(deliveryInfo.getWarehouse().getSenderName());
                consignee.setMobile(deliveryInfo.getWarehouse().getSenderMobile());
                consignee.setRegionId(deliveryInfo.getWarehouse().getRegionId());
                consignee.setAddress(deliveryInfo.getWarehouse().getAddress());
                // 设置仓库的联系人
                expressVo.setSender(consignee);
            }
            // 设置店铺物流
            expressVo.setShopLogisticId(deliveryInfo.getShopLogistics().getId());
        }
        Express newExpress = Express.builder().status(Express.UNSENT).shopLogisticsId(expressVo.getShopLogisticId()).shopId(shopId)
                .senderName(expressVo.getSender().getName()).senderMobile(expressVo.getSender().getMobile())
                .senderRegionId(expressVo.getSender().getRegionId()).senderAddress(expressVo.getSender().getAddress())
                .deliveryName(expressVo.getDelivery().getName()).deliveryMobile(expressVo.getDelivery().getMobile())
                .deliveryRegionId(expressVo.getDelivery().getRegionId()).deliveryAddress(expressVo.getDelivery().getAddress()).build();

        Express express;
        try{
            express = expressDao.insert(newExpress,user);
        }catch (RuntimeException e) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
        }
        // 通过shopLogistics生成相应的的adaptor
        ShopLogistics shopLogistics = express.getShopLogistics();
        ExpressAdaptor expressAdaptor = expressAdaptorFactory.createExpressAdaptorByShop(shopLogistics);

        // 通过adaptor向第三方获取申请运单(参数未定)
        // 返回申请运单后返回的运单信息并存入
        CreateExpressAdaptorDto createExpressAdaptorDto = expressAdaptor.createExpress(express);
        if(createExpressAdaptorDto.getStatus() == 0)
        {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR,String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
        }

        express.setBillCode(createExpressAdaptorDto.getBillCode());
        // 更新bo对象
        try{
            expressDao.saveById(express,user);
        }catch (RuntimeException e) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
        }

        SimplePackageDto simplePackageDto = new SimplePackageDto();
        simplePackageDto.setId(express.getId());
        simplePackageDto.setBillCode(express.getBillCode());
        return new ReturnObject(ReturnNo.CREATED,simplePackageDto);
    }

    /**
     * 获取运单
     * @param shopId
     * @param id
     * @param billCode
     * @param type
     * @return
     */
    @Transactional
    public ExpressDto getExpress(Long shopId,Long id,String billCode,byte type,UserDto user)
    {
        Express express;
        try{
            // 查询到未更新的运单
            if(type == 0)
                express = expressDao.findByBillCode(billCode);
            else
                express = expressDao.findById(id);
        }catch (RuntimeException e) {
            if (e.getMessage().equals(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage())) {
                throw e;
            } else {
                throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
            }
        }

        // 店铺id范围
        if (shopId != null && (!shopId.equals(PLATFORM) && !express.getShopId().equals(shopId))) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE,String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(),"shopId",shopId));
        }

        // 通过shopLogistics生成相应的的adaptor
        ShopLogistics shopLogistics = shopLogisticsDao.findById(express.getShopLogistics().getId());
        ExpressAdaptor expressAdaptor = expressAdaptorFactory.createExpressAdaptorByShop(shopLogistics);

        // 查询最新物流
        GetRouteInfoAdaptorDto getRouteInfoAdaptorDto =
                expressAdaptor.returnRouteInfoByBillCode(express.getBillCode());
        // 查询订单状态
        GetExpressAdaptorDto getExpressAdaptorDto =
                expressAdaptor.returnExpressByPackageId(String.valueOf(express.getId()));

        if(getRouteInfoAdaptorDto.getStatus() == 0 || getExpressAdaptorDto.getStatus() == 0)
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR,String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));

        boolean routeModified = false;

        // 物流状态是否长时间未更新
        LocalDateTime lastRouteTime = getRouteInfoAdaptorDto.getRoutes().get(getRouteInfoAdaptorDto.getRoutes().size()-1).getGmtCreate();
        Duration timeDis = Duration.between(lastRouteTime,LocalDateTime.now());
        if(timeDis.toDays() > LostRecordTime)
        {
            if(express.allowStatus(Express.LOST)) {
                express.setStatus(Express.LOST);
                routeModified = true;
            }
        }


        byte retExpressStatus;
        if(getExpressAdaptorDto.getExpressStatus() == -1){
            //顺丰的运单状态在路由信息中
            retExpressStatus = getRouteInfoAdaptorDto.getExpressStatus();
        }else {
            //极兔、中通的运单状态在运单信息中
            retExpressStatus = getExpressAdaptorDto.getExpressStatus();
        }

        if(!routeModified && retExpressStatus != express.getStatus())
        {
            if(!express.allowStatus(retExpressStatus))
                throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR,"不允许运单状态迁移");
            else{
                express.setStatus(retExpressStatus);
                routeModified = true;
            }
        }

        if(routeModified)
        {
            try {
                expressDao.saveById(express, user);
            } catch (RuntimeException e) {
                throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
            }
        }

        // 数据返回给ExpressDto
        SimpleLogisticsDto simpleLogisticsDto = SimpleLogisticsDto.builder().id(express.getShopLogistics().getLogisticsId())
                .name(express.getShopLogistics().getLogistics().getName()).build();
        ConsigneeDto sender = ConsigneeDto.builder().name(express.getSenderName()).mobile(express.getSenderMobile())
                .regionId(express.getSenderRegionId()).address(express.getSenderAddress()).build();
        ConsigneeDto delivery = ConsigneeDto.builder().name(express.getDeliveryName()).mobile(express.getDeliveryMobile())
                .regionId(express.getDeliveryRegionId()).address(express.getDeliveryAddress()).build();
        SimpleAdminUserDto creator = SimpleAdminUserDto.builder().id(express.getCreatorId()).userName(express.getCreatorName()).build();
        SimpleAdminUserDto modifier = SimpleAdminUserDto.builder().id(express.getModifierId()).userName(express.getModifierName()).build();

        List<RouteDto> routeDtos = new ArrayList<>();
        for(GetRouteInfoAdaptorDto.Route route : getRouteInfoAdaptorDto.getRoutes()) {
            routeDtos.add(RouteDto.builder().gmtCreate(route.getGmtCreate()).content(route.getContent()).build());
        }
        ExpressDto expressDto = ExpressDto.builder().id(express.getId()).billCode(express.getBillCode()).logistics(simpleLogisticsDto)
                .shipper(sender).receiver(delivery).creator(creator).modifier(modifier).status(express.getStatus())
                .gmtCreate(String.valueOf(express.getGmtCreate())).gmtModified(String.valueOf(express.getGmtModified()))
                .routes(routeDtos).build();
        return expressDto;
    }


    /**
     * 商家验收运单
     * 退回之后商家验收运单
     * 0-验收不合格 -> 破损
     * 1-验收合格 -> 回收
     * @param shopId
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject confirmExpress(Long shopId, Long id, ConfirmExpressVo confirmExpressVo, UserDto user) throws BusinessException
    {
        Express express;
        try{
            // 查询到未更新的运单
            express = expressDao.findById(id);
        }catch (RuntimeException e) {
            if (e.getMessage().equals(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage())) {
                throw e;
            } else {
                throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
            }
        }

        // 店铺id范围
        if (!shopId.equals(PLATFORM) && !express.getShopId().equals(shopId)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE,String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(),"shopId",shopId));
        }

        if(confirmExpressVo.getStatus() == 0)
        {
            if(express.allowStatus(Express.BROKEN))
                express.setStatus(Express.BROKEN);
            else
                throw new BusinessException(ReturnNo.STATENOTALLOW,String.format(ReturnNo.STATENOTALLOW.getMessage(),"运单",express.getId(),express.getStatusName()));
        }else if(confirmExpressVo.getStatus() == 1)
        {
            if(express.allowStatus(Express.RECYCLED))
                express.setStatus(Express.RECYCLED);
            else
                throw new BusinessException(ReturnNo.STATENOTALLOW,String.format(ReturnNo.STATENOTALLOW.getMessage(),"运单",express.getId(),express.getStatusName()));
        }else
            throw new BusinessException(ReturnNo.FIELD_NOTVALID,String.format(ReturnNo.FIELD_NOTVALID.getMessage(),"status"));

        try {
            expressDao.saveById(express,user);
        }catch (RuntimeException e) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
        }

        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商家取消运单
     * @param shopId
     * @param id
     * @return
     */
    @Transactional
    public ReturnObject cancelExpress(Long shopId, Long id, UserDto user) throws BusinessException
    {
        Express express;
        try{
            // 查询到未更新的运单
            express = expressDao.findById(id);
        }catch (RuntimeException e) {
            if (e.getMessage().equals(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage())) {
                throw e;
            } else {
                throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
            }
        }

        // 店铺id范围
        if (!shopId.equals(PLATFORM) && !express.getShopId().equals(shopId)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE,String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(),"shopId",shopId));
        }

        ShopLogistics shopLogistics = shopLogisticsDao.findById(express.getShopLogistics().getId());
        ExpressAdaptor expressAdaptor = expressAdaptorFactory.createExpressAdaptorByShop(shopLogistics);

        if(express.allowStatus(Express.CANCEL))
            express.setStatus(Express.CANCEL);
        else
            throw new BusinessException(ReturnNo.STATENOTALLOW,String.format(ReturnNo.STATENOTALLOW.getMessage(),"运单",express.getId(),express.getStatusName()));

        // 通过api取消运单
        CancelExpressAdaptorDto cancelExpressAdaptorDto =
                expressAdaptor.cancelExpressByPackageId(null,String.valueOf(express.getId()));

        // 判断cancelExpressAdaptorDto中的信息
        if(cancelExpressAdaptorDto.getStatus() == 0)
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR,String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
        try {
            expressDao.saveById(express,user);
        }catch (RuntimeException e) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, String.format(ReturnNo.INTERNAL_SERVER_ERR.getMessage()));
        }

        return new ReturnObject(ReturnNo.OK);
    }

}

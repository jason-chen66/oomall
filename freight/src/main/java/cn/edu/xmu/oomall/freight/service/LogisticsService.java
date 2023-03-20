package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.controller.vo.ShopLogisticsVo;
import cn.edu.xmu.oomall.freight.controller.vo.ShopUndeliverableRegionVo;
import cn.edu.xmu.oomall.freight.dao.LogisticsDao;
import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.UndeliverableDao;
import cn.edu.xmu.oomall.freight.dao.UndeliverableDao;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.dao.bo.Undeliverable;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.SimpleRegion;
import cn.edu.xmu.oomall.freight.service.dto.*;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.processing.RoundEnvironment;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;
import static cn.edu.xmu.javaee.core.util.Common.createPageObj;


@Service
public class LogisticsService {
    private static final Logger logger = LoggerFactory.getLogger(LogisticsService.class);

    private LogisticsDao logisticsDao;

    private ShopLogisticsDao shopLogisticsDao;

    private UndeliverableDao undeliverableDao;

    @Autowired
    public LogisticsService(LogisticsDao logisticsDao, ShopLogisticsDao shopLogisticsDao, UndeliverableDao undeliverableDao) {
        this.logisticsDao = logisticsDao;
        this.shopLogisticsDao = shopLogisticsDao;
        this.undeliverableDao = undeliverableDao;
    }


    /**
     * 根据物流单号查询属于哪家物流公司
     */
    public PageDto<SimpleLogisticsDto> findLogisticsByBillCode(String BillCode) {
        List<SimpleLogisticsDto> ret=null;
        ret=logisticsDao.findLogisticsByBillCode(BillCode);
        return new PageDto<>(ret,1,10);
    }

    /**
     * 将ShopLogistics的BO转换为DTO
     * @param bo
     * @return
     */
    private ShopLogisticsDto getShopLogisticsDto(ShopLogistics bo){
        return ShopLogisticsDto.builder().id(bo.getId())
                .logistics(SimpleLogisticsDto.builder().id(bo.getLogisticsId()).name(bo.getLogistics().getName()).build())
                .status(bo.getInvalid())
                .secret(bo.getSecret())
                .priority(bo.getPriority())
                .gmtCreate(bo.getGmtCreate())
                .gmtModified(bo.getGmtModified())
                .creator(SimpleAdminUserDto.builder().id(bo.getCreatorId()).userName(bo.getCreatorName()).build())
                .modifier(SimpleAdminUserDto.builder().id(bo.getCreatorId()).userName(bo.getCreatorName()).build()).build();
    }

    /**
     * 将Undeliverable的BO转换为DTO
     * @param bo
     * @return
     */
    private UndeliverableDto getUndeliverableDto(Undeliverable bo) {
        return UndeliverableDto.builder().region(SimpleRegionDto.builder().id(bo.getRegionId()).name(bo.getRegion().getName()).build())
                .id(bo.getId()).beginTime(bo.getBeginTime()).endTime(bo.getEndTime())
                .creator(SimpleAdminUserDto.builder().id(bo.getCreatorId()).userName(bo.getCreatorName()).build())
                .modifier(SimpleAdminUserDto.builder().id(bo.getModifierId()).userName(bo.getModifierName()).build())
                .gmtCreate(bo.getGmtCreate()).gmtModified(bo.getGmtModified()).build();
    }

    /**
     * 获得物流合作信息，按优先级排序
     * @param shopId
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageDto<ShopLogisticsDto> retrieveShopLogistics(Long shopId, Integer page, Integer pageSize) {
        PageInfo<ShopLogistics> shopShopLogisticsList = shopLogisticsDao.retrieveByShopId(shopId, page, pageSize);
        List<ShopLogisticsDto> ret=new ArrayList<>();
        if(null!=shopShopLogisticsList && shopShopLogisticsList.getList().size()>0){
            ret=shopShopLogisticsList.getList().stream()
                    .map(bo->getShopLogisticsDto(bo))
                    .sorted((dto1,dto2)->{if(dto1.getPriority()>dto2.getPriority()){return 1;} else return -1;})
                    .collect(Collectors.toList());
        }

        return new PageDto<>(ret,page,pageSize);
    }

    /**
     * 店家新增物流合作
     * 初始为无效
     */
    @Transactional(rollbackFor = Exception.class)
    public ShopLogisticsDto createShopLogistics(Long shopId, ShopLogisticsVo shopLogisticsVo, UserDto user) {
        //判断平台物流是否存在
        Logistics logistics=logisticsDao.findById(shopLogisticsVo.getLogisticsId());
        if(null==logistics)
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(),"物流渠道",shopLogisticsVo.getLogisticsId()));
        //店铺物流已存在
        ShopLogistics ret=shopLogisticsDao.findByShopIdAndLogisticsId(shopId,shopLogisticsVo.getLogisticsId());
        if(ret!=null)
            throw new BusinessException(ReturnNo.FREIGHT_LOGISTIC_EXIST,String.format(ReturnNo.FREIGHT_LOGISTIC_EXIST.getMessage(),ret.getId()));
        ShopLogistics shopLogistics=new ShopLogistics(shopId,shopLogisticsVo.getLogisticsId(), ShopLogistics.INVALID,shopLogisticsVo.getSecret(),shopLogisticsVo.getPriority());
        ShopLogistics bo = shopLogisticsDao.insert(shopLogistics,user);
        return getShopLogisticsDto(bo);
    }

    /**
     * 店家更新物流合作信息
     * 更新完状态为无效
     */
    @Transactional(rollbackFor = Exception.class)
    public ReturnObject updateShopLogistics(Long shopId,Long logisticsId, ShopLogisticsVo shopLogisticsVo, UserDto user) {

        ShopLogistics shopLogistics=ShopLogistics.builder().shopId(shopId)
                                                .logisticsId(logisticsId)
                                                .invalid(ShopLogistics.INVALID)
                                                .secret(shopLogisticsVo.getSecret())
                                                .priority(shopLogisticsVo.getPriority()).build();
        shopLogisticsDao.save(shopLogistics,user);
        return new ReturnObject();
    }

    /**
     * 店家更新物流状态
     */
    @Transactional(rollbackFor = Exception.class)
    public ReturnObject updateShopLogisticsStatus(Long shopId, Long id, Byte statue, UserDto user) {
        ShopLogistics shopLogistics= ShopLogistics.builder().shopId(shopId)
                .logisticsId(id).invalid(statue).build();
        shopLogisticsDao.save(shopLogistics,user);
        return new ReturnObject();
    }

    /**
     * 商户指定快递公司无法配送某个地区
     */
    @Transactional(rollbackFor = Exception.class)
    public ReturnObject createShopUndeliverableRegion(Long shopId, Long lid, Long rid, LocalDateTime beginTime, LocalDateTime endTime, UserDto user) {
        //信息已存在 错误码不知道
        if(undeliverableDao.findByShopLogisticsIdAndRegionId(lid,rid)!=null)
            throw new BusinessException(ReturnNo.FREIGHT_LOGISTIC_EXIST);
        Undeliverable undeliverable=Undeliverable.builder().regionId(rid).shopLogisticsId(lid).beginTime(beginTime).endTime(endTime).build();
        undeliverableDao.insert(undeliverable,user);
        return new ReturnObject();
    }


    /**
     * 商户更新不可达信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ReturnObject updateShopUndeliverableRegion(Long shopId, Long lid, Long rid, LocalDateTime beginTime, LocalDateTime endTime, UserDto user) {
        if(null==undeliverableDao.findByShopLogisticsIdAndRegionId(lid,rid))
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        Undeliverable undeliverable=Undeliverable.builder().shopLogisticsId(lid).regionId(rid).beginTime(beginTime).endTime(endTime).build();
        undeliverableDao.save(undeliverable,user);
        return new ReturnObject();
    }

    /**
     * 删除不可达信息
     * @param lid
     * @param rid
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ReturnObject deleteUndeliverableByShopLogisticsIdAndRegionId(Long shopId,Long lid, Long rid) {
        //信息不存在
        if(undeliverableDao.findByShopLogisticsIdAndRegionId(lid,rid)==null)
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(),"Undeliverable",lid));

        undeliverableDao.deleteByShopLogisticsIdAndRegionId(lid,rid);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商户查询快递公司无法配送的地区
     */
    @Transactional(readOnly = true)
    public PageDto<UndeliverableDto> retrieveUndeliverableByShopLogisticsId(Long shopId,Long lid, Integer page, Integer pageSize) {

        PageInfo<Undeliverable> undeliverablePageInfo = undeliverableDao.retrieveByShopLogisticsId(lid,page,pageSize);
        List<UndeliverableDto> ret=new ArrayList<>();
        if(null!=undeliverablePageInfo&&undeliverablePageInfo.getList().size()>0){
            ret=undeliverablePageInfo.getList().stream().map(bo->getUndeliverableDto(bo)).collect(Collectors.toList());
            return new PageDto<>(ret,page,pageSize);
        }
        else
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(),"Undeliverable",lid));

    }

}


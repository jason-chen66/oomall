package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.ShopLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.WarehouseDao;
import cn.edu.xmu.oomall.freight.dao.WarehouseLogisticsDao;
import cn.edu.xmu.oomall.freight.dao.WarehouseRegionDao;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseLogistics;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.service.dto.*;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@Service
public class WarehouseService {

    private WarehouseDao warehouseDao;

    private WarehouseRegionDao warehouseRegionDao;

    private WarehouseLogisticsDao warehouseLogisticsDao;

    private RegionDao regionDao;

    private ShopLogisticsDao shopLogisticsDao;

    @Autowired
    public WarehouseService(WarehouseDao warehouseDao, WarehouseRegionDao warehouseRegionDao, WarehouseLogisticsDao warehouseLogisticsDao, RegionDao regionDao, ShopLogisticsDao shopLogisticsDao) {
        this.warehouseDao = warehouseDao;
        this.warehouseRegionDao = warehouseRegionDao;
        this.warehouseLogisticsDao = warehouseLogisticsDao;
        this.regionDao = regionDao;
        this.shopLogisticsDao = shopLogisticsDao;
    }

    /**
     * 查询某个地区可以配送的所有仓库
     * - 地区可以用下级地区查询(在 dao 层实现)
     * - 仓库按优先级从高到低返回(排序在 mapper 层实现)
     *
     * @param shopId
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional
    public PageDto<RegionWarehouseDto> retrieveRegionWarehouses(Long shopId, Long id, Integer page, Integer pageSize) {
        PageInfo<WarehouseRegion> warehouseRegionPageInfo = new PageInfo<>();
        if(PLATFORM == shopId){
            warehouseRegionPageInfo = warehouseRegionDao.retrieveAllByRegionIdAdmin(shopId, id, page, pageSize);
        }else{
            warehouseRegionPageInfo = warehouseRegionDao.retrieveAllByRegionId(shopId, id, page, pageSize);
        }
        List<RegionWarehouseDto> regionWarehouseDtoList = warehouseRegionPageInfo.getList().stream().map(bo -> {
            Warehouse warehouse = bo.getWarehouse();
            RegionWarehouseDto regionWarehouseDto = RegionWarehouseDto.builder()
                    .beginTime(bo.getBeginTime()).endTime(bo.getEndTime())
                    .warehouse(SimpleWarehouseDto.builder().id(warehouse.getId()).name(warehouse.getName()).invalid(warehouse.getInvalid()).priority(warehouse.getPriority()).build())
                    .gmtCreate(bo.getGmtCreate()).gmtModified(bo.getGmtModified())
                    .creator(SimpleAdminUserDto.builder().id(bo.getCreatorId()).userName(bo.getCreatorName()).build())
                    .modifier(SimpleAdminUserDto.builder().id(bo.getModifierId()).userName(bo.getModifierName()).build()).build();
            return regionWarehouseDto;
        }).collect(Collectors.toList());
        return new PageDto<>(regionWarehouseDtoList, warehouseRegionPageInfo.getPageNum(), warehouseRegionPageInfo.getPageSize());
    }

    /**
     * 将WarehouseRegion的BO转换为DTO
     * @param bo
     * @return
     */
    private WarehouseRegionDto getWarehouseRegionDto(WarehouseRegion bo){
        return WarehouseRegionDto.builder()
                .region(SimpleRegionDto.builder().id(bo.getRegionId()).name(bo.getRegion().getName()).build())
                .beginTime(bo.getBeginTime()).endTime(bo.getEndTime())
                .gmtCreate(bo.getGmtCreate()).gmtModified(bo.getGmtModified())
                .creator(SimpleAdminUserDto.builder().id(bo.getCreatorId()).userName(bo.getCreatorName()).build())
                .modifier(SimpleAdminUserDto.builder().id(bo.getModifierId()).userName(bo.getModifierName()).build()).build();
    }
    /**
     * 商户新增仓库配送地区
     *
     * @param warehouseId
     * @param regionId
     * @param beginTime
     * @param endTime
     * @param user
     * @return
     */
    @Transactional(rollbackFor = BusinessException.class)
    public WarehouseRegionDto createWarehouseRegion(Long shopId, Long warehouseId, Long regionId, LocalDateTime beginTime, LocalDateTime endTime, UserDto user) {
        if (beginTime.isAfter(endTime)){
            throw new BusinessException(ReturnNo.LATE_BEGINTIME);
        }
        if(warehouseDao.findById(warehouseId) == null){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "仓库", warehouseId));
        }
        if(regionDao.getRegionById(regionId).getErrno() == ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "地区", regionId));
        }
        if(warehouseRegionDao.findByWarehouseIdAndRegionId(warehouseId, regionId) != null) {
            throw new BusinessException(ReturnNo.FREIGHT_WAREHOUSEREGION_EXIST);
        }
        WarehouseRegion warehouseRegion=WarehouseRegion.builder().warehouseId(warehouseId).regionId(regionId).beginTime(beginTime).endTime(endTime).build();
        WarehouseRegion bo=warehouseRegionDao.insert(warehouseRegion,user,shopId);
        return getWarehouseRegionDto(bo);
    }


    /**
     * 商户修改仓库配送地区
     * - 如果仓库没有此地区设置，则不做任何操作
     * @param warehouseId
     * @param regionId
     * @param beginTime
     * @param endTime
     * @param user
     * @return
     */
    @Transactional(rollbackFor = BusinessException.class)
    public ReturnNo updateWarehouseRegions(Long shopId, Long warehouseId,Long regionId,LocalDateTime beginTime, LocalDateTime endTime, UserDto user) {
        if (beginTime.isAfter(endTime)){
            throw new BusinessException(ReturnNo.LATE_BEGINTIME);
        }
        if(warehouseRegionDao.findByWarehouseIdAndRegionId(warehouseId, regionId) == null) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,"没有此仓库配送地区");
        }
        WarehouseRegion warehouseRegion = WarehouseRegion.builder().warehouseId(warehouseId).regionId(regionId).beginTime(beginTime).endTime(endTime).build();
        warehouseRegionDao.save(warehouseRegion,user, shopId);
        return ReturnNo.OK;
    }

    /**
     * 商户删除仓库配送地区
     * - 直接物理删除
     * @param warehouseId
     * @param regionId
     * @return
     */
    @Transactional(rollbackFor = BusinessException.class)
    public ReturnNo deleteWarehouseRegion(Long shopId,Long warehouseId,Long regionId) {
        WarehouseRegion bo = WarehouseRegion.builder().warehouseId(warehouseId).regionId(regionId).build();
        warehouseRegionDao.delete(bo,shopId);
        return ReturnNo.OK;
    }

    /**
     * 商户或管理员查询某个仓库的配送地区
     * @param warehouseId
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional
    public PageDto<WarehouseRegionDto> retrieveWarehousesRegionsByWarehouseId(Long warehouseId,Integer page, Integer pageSize) {
        PageInfo<WarehouseRegion> warehouseRegionPageInfo = warehouseRegionDao.retrieveByWarehouseId(warehouseId,page,pageSize);
        List<WarehouseRegionDto> ret=new ArrayList<>();
        if(null!=warehouseRegionPageInfo && warehouseRegionPageInfo.getList().size()>0){
            ret=warehouseRegionPageInfo.getList().stream().map(bo->getWarehouseRegionDto(bo)).collect(Collectors.toList());
        }
        return new PageDto<>(ret,page,pageSize);
    }

    /**
     * 将Warehouse的BO转换为DTO
     * @param bo
     * @return
     */
    private WarehouseDto getWarehouseDto(Warehouse bo){
        return WarehouseDto.builder().id(bo.getId()).name(bo.getName()).address(bo.getAddress())
                .region(SimpleRegionDto.builder().id(bo.getRegionId()).name(bo.getRegion().getName()).build())
                .senderName(bo.getSenderName()).senderMobile(bo.getSenderMobile())
                .invalid(bo.getInvalid()).priority(bo.getPriority())
                .gmtCreate(bo.getGmtCreate()).gmtModified(bo.getGmtModified())
                .creator(SimpleAdminUserDto.builder().id(bo.getCreatorId()).userName(bo.getCreatorName()).build())
                .modifier(SimpleAdminUserDto.builder().id(bo.getModifierId()).userName(bo.getModifierName()).build()).build();
    }

    /**
     * 商家创建仓库
     * @param name
     * @param address
     * @param regionId
     * @param senderName
     * @param senderMobile
     * @param shopId
     * @param user
     * @return
     */
    @Transactional(rollbackFor = BusinessException.class)
    public WarehouseDto createWarehouse(String name, String address, Long regionId, String senderName, String senderMobile, Long shopId, UserDto user){
        if (shopId.equals(PLATFORM)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE,"平台管理员不能创建仓库");
        }
        Warehouse warehouse=new Warehouse(name,address,regionId,senderName,senderMobile,shopId);
        Warehouse bo = warehouseDao.insert(warehouse,user);
        return getWarehouseDto(bo);
    }


    /**
     * 获得仓库，按优先级排序
     * - 如果是管理员的话，获得所有仓库
     * - 如果是商户的话，获得商户的仓库
     * -排序做在 mapper 层,因为他是分页查询
     * @param shopId
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional
    public PageDto<WarehouseDto> retrieveWarehouses(Long shopId,Integer page,Integer pageSize){
        PageInfo<Warehouse> warehousePageInfo = warehouseDao.retrieveByShopId(shopId,page,pageSize);
        List<WarehouseDto> ret=new ArrayList<>();
        if(null!=warehousePageInfo && warehousePageInfo.getList().size()>0){
            ret=warehousePageInfo.getList().stream().map(bo->getWarehouseDto(bo)).collect(Collectors.toList());
        }
        return new PageDto<>(ret,page,pageSize);
    }

    /**
     * 修改仓库
     * @param id
     * @param name
     * @param address
     * @param regionId
     * @param senderName
     * @param senderMobile
     * @param user
     * @return
     */
    @Transactional(rollbackFor = BusinessException.class)
    public ReturnNo updateWarehouses(Long shopId, Long id, String name, String address, Long regionId, String senderName, String senderMobile, UserDto user) {
        Warehouse warehouse = Warehouse.builder().shopId(shopId).id(id).name(name).address(address).regionId(regionId).senderName(senderName).senderMobile(senderMobile).build();
        warehouseDao.save(warehouse,user);
        return ReturnNo.OK;
    }


    /**
     * 删除仓库
     * -需删除仓库与物流，仓库与地区的关系
     * -物理删除
     */
    @Transactional(rollbackFor = BusinessException.class)
    public ReturnNo deleteWarehouse(Long shopId, Long id) {
        Warehouse warehouse = Warehouse.builder().id(id).shopId(shopId)
                .warehouseLogisticsDao(warehouseLogisticsDao)
                .warehouseRegionDao(warehouseRegionDao).build();
        warehouse.getWarehouseLogisticsList().forEach(bo->{warehouseLogisticsDao.delete(bo);});
        warehouse.getWarehouseRegionList().forEach(bo->{warehouseRegionDao.delete(bo,shopId);});
        warehouseDao.delete(warehouse);
        return ReturnNo.OK;
    }

    /**
     * 更新仓库库的状态
     * @param id
     * @param status
     * @return
     */
    @Transactional(rollbackFor = BusinessException.class)
    public ReturnNo updateWarehouseStatus(Long shopId, Long id, Byte status, UserDto user) {
        Warehouse warehouse = Warehouse.builder().shopId(shopId).id(id).invalid(status).build();
        warehouseDao.save(warehouse, user);
        return ReturnNo.OK;
    }

    /**
     * 将WarehouseLogistics的BO转换为DTO
     * @param bo
     * @return
     */
    private WarehouseLogisticsDto getWarehouseLogisticsDto(WarehouseLogistics bo){
        ShopLogistics sl = bo.getShopLogistics();
        ShopLogisticsDto shopLogisticsDto = ShopLogisticsDto.builder()
                .id(sl.getId())
                .logistics(SimpleLogisticsDto.builder().id(sl.getLogisticsId()).name(sl.getLogistics().getName()).build())
                .status(sl.getInvalid()).secret(sl.getSecret())
                .priority(sl.getPriority()).gmtCreate(sl.getGmtCreate()).gmtModified(sl.getGmtModified())
                .creator(SimpleAdminUserDto.builder().id(sl.getCreatorId()).userName(sl.getCreatorName()).build())
                .modifier(SimpleAdminUserDto.builder().id(sl.getModifierId()).userName(sl.getModifierName()).build()).build();

        return WarehouseLogisticsDto.builder()
                .shopLogistics(shopLogisticsDto)
                .beginTime(bo.getBeginTime()).endTime(bo.getEndTime()).invalid(bo.getInvalid())
                .gmtCreate(bo.getGmtCreate()).gmtModified(bo.getGmtModified())
                .creator(SimpleAdminUserDto.builder().id(bo.getCreatorId()).userName(bo.getCreatorName()).build())
                .modifier(SimpleAdminUserDto.builder().id(bo.getModifierId()).userName(bo.getModifierName()).build()).build();
    }

    /**
     * 创建仓库物流
     * @param warehouseId
     * @param shopLogisticsId
     * @param beginTime
     * @param endTime
     * @param user
     * @return
     */
    @Transactional(rollbackFor = BusinessException.class)
    public WarehouseLogisticsDto createWarehouseLogistics(Long warehouseId, Long shopLogisticsId, LocalDateTime beginTime, LocalDateTime endTime, UserDto user) {
        if (beginTime.isAfter(endTime)){
            throw new BusinessException(ReturnNo.LATE_BEGINTIME);
        }
        if(warehouseDao.findById(warehouseId) == null){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "仓库", warehouseId));
        }
        if(shopLogisticsDao.findById(shopLogisticsId) == null){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "店铺物流", shopLogisticsId));
        }
        if(warehouseLogisticsDao.findByWarehouseIdAndShopLogisticsId(warehouseId,shopLogisticsId) != null){
            throw new BusinessException(ReturnNo.FREIGHT_WAREHOUSELOGISTIC_EXIST);
        }
        WarehouseLogistics warehouseLogistics = new WarehouseLogistics(warehouseId, shopLogisticsId, beginTime, endTime);
        WarehouseLogistics bo = warehouseLogisticsDao.insert(warehouseLogistics, user);
        return getWarehouseLogisticsDto(bo);
    }

    /**
     *商户修改仓库物流信息
     * @param warehouseId
     * @param shopLogisticsId
     * @param beginTime
     * @param endTime
     * @param user
     * @return
     */
    @Transactional(rollbackFor = BusinessException.class)
    public ReturnNo updateWarehosueLogistics(Long warehouseId, Long shopLogisticsId, LocalDateTime beginTime, LocalDateTime endTime, UserDto user) {
        if (beginTime.isAfter(endTime)){
            throw new BusinessException(ReturnNo.LATE_BEGINTIME);
        }
        WarehouseLogistics warehouseLogistics = WarehouseLogistics.builder().warehouseId(warehouseId).shopLogisticsId(shopLogisticsId).beginTime(beginTime).endTime(endTime).build();
        warehouseLogisticsDao.save(warehouseLogistics,user);
        return ReturnNo.OK;
    }

    /**
     * 商户删除仓库物流配送关系
     *
     * @param warehouseId
     * @param shopLogisticsId
     * @param user
     * @return
     */
    @Transactional(rollbackFor = BusinessException.class)
    public ReturnNo deleteWarehosueLogistics(Long warehouseId, Long shopLogisticsId, UserDto user) {
        WarehouseLogistics warehouseLogistics = WarehouseLogistics.builder().warehouseId(warehouseId).shopLogisticsId(shopLogisticsId).build();
        warehouseLogisticsDao.delete(warehouseLogistics);
        return ReturnNo.OK;
    }


    /**
     * 获得仓库物流
     *  -按照优先级从小往大排序
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    public PageDto<WarehouseLogisticsDto> retrieveWarehouseLogistics(Long id, Integer page, Integer pageSize) {
        PageInfo<WarehouseLogistics> warehouseLogisticsPageInfo = warehouseLogisticsDao.retrieveByWarehouseId(id,page,pageSize);
        List<WarehouseLogisticsDto> ret=new ArrayList<>();
        if(null!=warehouseLogisticsPageInfo && warehouseLogisticsPageInfo.getList().size()>0){
            ret=warehouseLogisticsPageInfo.getList().stream()
                    .sorted((bo1,bo2)->{if (bo1.getShopLogistics().getPriority() > bo2.getShopLogistics().getPriority()) {return 1;} else {return -1;}})
                    .map(bo->getWarehouseLogisticsDto(bo))
                    .collect(Collectors.toList());
        }
        return new PageDto<>(ret,page,pageSize);
    }


}

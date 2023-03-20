//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.controller.vo.*;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.service.LogisticsService;
import cn.edu.xmu.oomall.freight.service.WarehouseService;
import cn.edu.xmu.oomall.freight.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 管理人员的接口
 */
@RestController
@RequestMapping(value = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
public class AdminFreightController {

    private final Logger logger = LoggerFactory.getLogger(AdminFreightController.class);

    private final LogisticsService logisticsService;

    private final WarehouseService warehouseService;

    @Autowired
    public AdminFreightController(LogisticsService logisticsService,WarehouseService warehouseService) {
        this.logisticsService = logisticsService;
        this.warehouseService= warehouseService;
    }

    /**
     * 店家获得物流合作信息
     * 按照优先级从小到大返回
     */
    @Audit(departName = "shops")
    @GetMapping("/shoplogistics")
    public ReturnObject retrieveShopLogistics(@PathVariable Long shopId,
                                              @RequestParam(required = false, defaultValue = "1") Integer page,
                                              @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        PageDto<ShopLogisticsDto> pageDto = logisticsService.retrieveShopLogistics(shopId, page, pageSize);
        return new ReturnObject(pageDto);
    }

    /**
     * 店家新增物流合作
     */
    @Audit(departName = "shops")
    @PostMapping("/shoplogistics")
    public ReturnObject createShopLogistics(@PathVariable("shopId") Long shopId,
                                            @Validated @RequestBody ShopLogisticsVo shopLogisticsVo,
                                            @LoginUser UserDto user) {

        ShopLogisticsDto dto = logisticsService.createShopLogistics(shopId, shopLogisticsVo, user);
        return new ReturnObject(ReturnNo.CREATED, dto);
    }

    /**
     * 店家更新物流合作信息
     */
    @Audit(departName = "shops")
    @PutMapping("/shoplogistics/{id}")
    public ReturnObject updateShopLogistics(@PathVariable("shopId") Long shopId,
                                            @PathVariable("id") Long shopLogisticsId,
                                            @Validated @RequestBody ShopLogisticsVo shopLogisticsVo,
                                            @LoginUser UserDto user) {

        return logisticsService.updateShopLogistics(shopId, shopLogisticsId,shopLogisticsVo, user);
    }

    /**
     * 商铺停用某个物流合作
     */
    @Audit(departName = "shops")
    @PutMapping("/shoplogistics/{id}/suspend")
    public ReturnObject updateShopLogisticsInValid(@PathVariable("shopId") Long shopId,
                                                 @PathVariable("id") Long id,
                                                 @LoginUser UserDto user) {

        return logisticsService.updateShopLogisticsStatus(shopId, id, ShopLogistics.INVALID, user);
    }

    /**
     * 商铺恢复某个物流合作
     */
    @Audit(departName = "shops")
    @PutMapping("/shoplogistics/{id}/resume")
    public ReturnObject updateShopLogisticsValid(@PathVariable("shopId") Long shopId,
                                                   @PathVariable("id") Long id,
                                                   @LoginUser UserDto user) {

        return logisticsService.updateShopLogisticsStatus(shopId, id, ShopLogistics.VALID, user);
    }

    /**
     * 根据物流单号查询属于哪家物流公司
     */
    @Audit(departName = "shops")
    @GetMapping("/logistics")
    public ReturnObject findLogisticsNameByBillCode(@RequestParam(required = false) String billCode) {
        PageDto<SimpleLogisticsDto> dto=logisticsService.findLogisticsByBillCode(billCode);
        return new ReturnObject(dto);
    }


    /**
     * 商户指定快递公司无法配送某个地区
     */
    @Audit(departName = "shops")
    @PostMapping("/shoplogistics/{id}/regions/{rid}/undeliverable")
    public ReturnObject createShopUndeliverableRegion(@PathVariable("shopId") Long shopId,
                                                      @PathVariable("id") Long id,
                                                      @PathVariable("rid") Long rid,
                                                      @Validated @RequestBody ShopUndeliverableRegionVo shopUndeliverableRegionVo,
                                                      @LoginUser UserDto user) {

        return logisticsService.createShopUndeliverableRegion(
                shopId, id, rid, shopUndeliverableRegionVo.getBeginTime(), shopUndeliverableRegionVo.getEndTime(), user);
    }

    /**
     * 商户更新不可达信息
     */
    @Audit(departName = "shops")
    @PutMapping("/shoplogistics/{id}/regions/{rid}/undeliverable")
    public ReturnObject updateShopUndeliverableRegion(@PathVariable("shopId") Long shopId,
                                                      @PathVariable("id") Long id,
                                                      @PathVariable("rid") Long rid,
                                                      @Validated @RequestBody ShopUndeliverableRegionVo shopUndeliverableRegionVo,
                                                      @LoginUser UserDto user) {

        return logisticsService.updateShopUndeliverableRegion(
                shopId, id, rid, shopUndeliverableRegionVo.getBeginTime(), shopUndeliverableRegionVo.getEndTime(), user);
    }

    /**
     * 商户删除某个不可达信息
     */
    @DeleteMapping("/shoplogistics/{id}/regions/{rid}/undeliverable")
    public ReturnObject deleteShopUndeliverableRegion(@PathVariable("shopId") Long shopId,
                                                      @PathVariable("id") Long id,
                                                      @PathVariable("rid") Long rid) {

        return logisticsService.deleteUndeliverableByShopLogisticsIdAndRegionId(shopId,id, rid);
    }


    /**
     * 商户查询快递公司无法配送的地区
     */
    @Audit(departName = "shops")
    @GetMapping("/shoplogistics/{id}/undeliverableregions")
    public ReturnObject retrieveShopUndeliverableRegions(@PathVariable Long shopId,
                                                         @PathVariable Long id,
                                                         @RequestParam(required = false, defaultValue = "1") Integer page,
                                                         @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        PageDto<UndeliverableDto> pageDto = logisticsService.retrieveUndeliverableByShopLogisticsId(shopId,id, page, pageSize);
        return new ReturnObject(pageDto);
    }

    /**
     *商户或管理员查询某个地区可以配送的所有仓库
     *- 地区可以用下级地区查询
     *- 仓库按优先级从高到低返回
     */
    @GetMapping("regions/{id}/warehouses")
    @Audit(departName = "shops")
    public ReturnObject retrieveRegionWarehouses(@PathVariable("shopId") Long shopId,
                                                 @PathVariable("id") Long id,
                                                 @RequestParam(required = false,defaultValue = "1") Integer page,
                                                 @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        PageDto<RegionWarehouseDto> pageDto = warehouseService.retrieveRegionWarehouses(shopId, id, page, pageSize);
        return new ReturnObject(pageDto);
    }

    /**
     * 商户新增仓库配送地区
     * - 需检查warehouse和region是否存在
     * - 重复出997错误
     */
    @PostMapping("/warehouses/{wid}/regions/{id}")
    @Audit(departName = "shops")
    public ReturnObject createWarehouseRegions(@PathVariable("shopId") Long shopId,
                                               @PathVariable("wid") Long wid,
                                               @PathVariable("id") Long id,
                                               @Validated @RequestBody WarehouseRegionVo vo,
                                               @LoginUser UserDto user){
        WarehouseRegionDto ret=warehouseService.createWarehouseRegion(shopId, wid,id,vo.getBeginTime(),vo.getEndTime(),user);
        return new ReturnObject(ReturnNo.CREATED,ret);
    }


    /**
     * 商户修改仓库配送地区
     *- 如果仓库没有此地区设置，则不做任何操作
     */
    @PutMapping("/warehouses/{wid}/regions/{id}")
    @Audit(departName = "shops")
    public ReturnObject updateWarehouseRegions(@PathVariable("shopId") Long shopId,
                                               @PathVariable("wid") Long wid,
                                               @PathVariable("id") Long id,
                                               @Validated @RequestBody WarehouseRegionVo vo,
                                               @LoginUser UserDto user){
        ReturnNo ret=warehouseService.updateWarehouseRegions(shopId,wid,id,vo.getBeginTime(),vo.getEndTime(),user);
        return new ReturnObject(ret);
    }

    /**
     * 商户删除仓库配送地区
     */
    @DeleteMapping("/warehouses/{wid}/regions/{id}")
    @Audit(departName = "shops")
    public ReturnObject deleteWarehouseRegion(@PathVariable("shopId") Long shopId,
                                              @PathVariable("wid") Long wid,
                                              @PathVariable("id") Long id){
        ReturnNo ret=warehouseService.deleteWarehouseRegion(shopId, wid,id);
        return new ReturnObject(ret);
    }

    /**
     * 商户或管理员查询某个仓库的配送地区
     */
    @GetMapping("/warehouses/{id}/regions")
    @Audit(departName = "shops")
    public ReturnObject retrieveWarehousesRegions(@PathVariable("id") Long id,
                                                  @RequestParam(required = false,defaultValue = "1") Integer page,
                                                  @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        PageDto<WarehouseRegionDto> ret=warehouseService.retrieveWarehousesRegionsByWarehouseId(id,page,pageSize);
        return new ReturnObject(ret);
    }

    /**
     * 商户新建仓库
     */
    @PostMapping("/warehouses")
    @Audit(departName = "shops")
    public ReturnObject createWarehouse(@PathVariable("shopId") Long shopId,
                                        @Validated @RequestBody WarehouseVo warehouseVo,
                                        @LoginUser UserDto user){
        WarehouseDto ret=warehouseService.createWarehouse(warehouseVo.getName(),warehouseVo.getAddress()
                ,warehouseVo.getRegionId(),warehouseVo.getSenderName(),warehouseVo.getSenderMobile(),shopId, user);
        return new ReturnObject(ReturnNo.CREATED,ret);
    }

    /**
     * 获得仓库
     * -按照优先级排序
     */
    @GetMapping("/warehouses")
    @Audit(departName = "shops")
    public ReturnObject retrieveWarehouses(@PathVariable("shopId") Long shopId,
                                           @RequestParam(required = false,defaultValue = "1") Integer page,
                                           @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        PageDto<WarehouseDto> ret=warehouseService.retrieveWarehouses(shopId,page,pageSize);
        return new ReturnObject(ret);
    }

    /**
     *商家修改仓库信息
     */
    @PutMapping("/warehouses/{id}")
    @Audit(departName = "shops")
    public ReturnObject updateWarehouse(@PathVariable("shopId") Long shopId,
                                        @PathVariable("id") Long id,
                                        @Validated @RequestBody WarehouseVo vo,
                                        @LoginUser UserDto user){
        ReturnNo ret=warehouseService.updateWarehouses(shopId,id,vo.getName(),vo.getAddress(),vo.getRegionId(),vo.getSenderName(),vo.getSenderMobile(),user);
        return new ReturnObject(ret);
    }

    /**
     * 商家删除仓库
     * -需删除仓库与物流，仓库与地区的关系
     */
    @DeleteMapping("/warehouses/{id}")
    @Audit(departName = "shops")
    public ReturnObject deleteWarehouse(@PathVariable("shopId") Long shopId,
                                        @PathVariable("id") Long id,
                                        @LoginUser UserDto user){
        ReturnNo ret=warehouseService.deleteWarehouse(shopId,id);
        return new ReturnObject(ret);
    }

    /**
     * 商铺暂停某个仓库发货
     */
    @PutMapping("/warehouse/{id}/suspend")
    @Audit(departName = "shops")
    public ReturnObject updateWarehouseInvalid(@PathVariable("shopId") Long shopId,
                                               @PathVariable("id") Long id,
                                               @LoginUser UserDto user){
        ReturnNo ret=warehouseService.updateWarehouseStatus(shopId,id, Warehouse.INVALID,user);
        return new ReturnObject(ret);
    }

    /**
     * 商铺恢复某个仓库发货
     */
    @PutMapping("/warehouse/{id}/resume")
    @Audit(departName = "shops")
    public ReturnObject updateWarehouseValid(@PathVariable("shopId") Long shopId,
                                             @PathVariable("id") Long id,
                                             @LoginUser UserDto user){
        ReturnNo ret=warehouseService.updateWarehouseStatus(shopId,id, Warehouse.VALID,user);
        return new ReturnObject(ret);
    }

    /**
     * 商户新建仓库物流
     */
    @PostMapping("/warehouses/{id}/shoplogistics/{lid}")
    @Audit(departName = "shops")
    public ReturnObject createWarehosueLogistics(@PathVariable("id") Long id,
                                                   @PathVariable("lid") Long lid,
                                                   @Validated @RequestBody WarehosueLogisticsVo vo,
                                                   @LoginUser UserDto user){
        if (vo.getBeginTime().isAfter(vo.getEndTime())){
            throw new BusinessException(ReturnNo.LATE_BEGINTIME);
        }
        WarehouseLogisticsDto ret=warehouseService.createWarehouseLogistics(id,lid,vo.getBeginTime(),vo.getEndTime(),user);
        return new ReturnObject(ReturnNo.CREATED,ret);
    }

    /**
     * 商户修改仓库物流信息
     */
    @PutMapping("/warehouses/{id}/shoplogistics/{lid}")
    @Audit(departName = "shops")
    public ReturnObject updateWarehosueLogistics(@PathVariable("id") Long id,
                                                   @PathVariable("lid") Long lid,
                                                   @Validated @RequestBody WarehosueLogisticsVo vo,
                                                   @LoginUser UserDto user){
        ReturnNo ret=warehouseService.updateWarehosueLogistics(id,lid,vo.getBeginTime(),vo.getEndTime(),user);
        return new ReturnObject(ret);
    }

    /**
     * 商户删除仓库物流配送关系
     */
    @DeleteMapping("/warehouses/{id}/shoplogistics/{lid}")
    @Audit(departName = "shops")
    public ReturnObject deleteWarehosueLogistics(@PathVariable("id") Long id,
                                                   @PathVariable("lid") Long lid,
                                                   @LoginUser UserDto user){
        ReturnNo ret=warehouseService.deleteWarehosueLogistics(id,lid,user);
        return new ReturnObject(ret);
    }


    /**
     * 获得仓库物流
     * -按照优先级从小往大排序
     */
    @GetMapping("/warehouses/{id}/shoplogistics")
    @Audit(departName = "shops")
    public ReturnObject retrieveWarehousesLogistics(@PathVariable("id") Long id,
                                                          @RequestParam(required = false,defaultValue = "1") Integer page,
                                                          @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        PageDto<WarehouseLogisticsDto> ret=warehouseService.retrieveWarehouseLogistics(id,page,pageSize);
        return new ReturnObject(ret);
    }


}

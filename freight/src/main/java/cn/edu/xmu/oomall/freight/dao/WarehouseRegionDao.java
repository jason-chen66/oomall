package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.SimpleRegion;
import cn.edu.xmu.oomall.freight.mapper.generator.WarehouseRegionPoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.po.*;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehouseRegionPo;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;
import static cn.edu.xmu.javaee.core.util.Common.*;

@Repository
public class WarehouseRegionDao {

    private final static Logger logger = LoggerFactory.getLogger(WarehouseRegionDao.class);

    //%d为仓库id和地区id
    public final static String WAREHOUSE_REGION_KEY = "FW%dR%d";

    public final static String REGION_WAREHOUSE_LIST_INTERNAL_KEY = "FRWLI%dS%d";

    public final static String WAREHOUSE_REGION_LIST_INTERNAL_KEY = "FWRLI%d";

    @Value("${oomall.freight.warehouse-region.timeout}")
    private int timeout;

    private RedisUtil redisUtil;

    private WarehouseRegionPoMapper warehouseRegionPoMapper;

    private WarehouseDao warehouseDao;

    private RegionDao regionDao;

    @Autowired
    @Lazy
    public WarehouseRegionDao(RedisUtil redisUtil, WarehouseRegionPoMapper warehouseRegionPoMapper, WarehouseDao warehouseDao, RegionDao regionDao) {
        this.redisUtil = redisUtil;
        this.warehouseRegionPoMapper = warehouseRegionPoMapper;
        this.warehouseDao = warehouseDao;
        this.regionDao = regionDao;
    }

    private WarehouseRegion getBo(WarehouseRegionPo po){
        WarehouseRegion bo=WarehouseRegion.builder().id(po.getId()).creatorId(po.getCreatorId()).creatorName(po.getCreatorName()).gmtCreate(po.getGmtCreate()).gmtModified(po.getGmtModified()).modifierId(po.getModifierId()).modifierName(po.getModifierName())
                .regionId(po.getRegionId()).warehouseId(po.getWarehouseId()).beginTime(po.getBeginTime()).endTime(po.getEndTime()).build();
        this.setBo(bo);
        return bo;
    }

    private void setBo(WarehouseRegion bo){
        bo.setWarehouseDao(this.warehouseDao);
        bo.setRegionDao(this.regionDao);
    }

    private WarehouseRegionPo getPo(WarehouseRegion bo){
        return WarehouseRegionPo.builder().warehouseId(bo.getWarehouseId()).regionId(bo.getRegionId())
                .beginTime(bo.getBeginTime()).endTime(bo.getEndTime()).build();
    }

    private void clearRedis(Long warehouseId, Long regionId, Long shopId){
        redisUtil.del(String.format(WAREHOUSE_REGION_KEY, warehouseId, regionId));
        redisUtil.del(String.format(REGION_WAREHOUSE_LIST_INTERNAL_KEY, regionId, shopId));
        redisUtil.del(String.format(WAREHOUSE_REGION_LIST_INTERNAL_KEY, warehouseId));
    }

    /**
     * 用地区id查询可以配送的所有仓库(普通店铺)
     * -包括上级地区的仓库
     * - 在 mapper 层用连表查询实现了排序
     * @param shopId
     * @param id
     * @return
     */
    public PageInfo<WarehouseRegion> retrieveAllByRegionId(Long shopId, Long id, Integer page, Integer pageSize) {
        List<WarehouseRegion> ret=new ArrayList<>();
        if(null != shopId && null != id){
            List<Long> warehouseIdList = warehouseDao.retrieveByShopIdInternal(shopId).stream().map(bo->bo.getId()).collect(Collectors.toList());
            if(warehouseIdList.size() > 0){
                List<Long> regionList = regionDao.getParentRegionsById(id).getData().stream().map(SimpleRegion::getId).collect(Collectors.toList());
                regionList.add(id);
                PageHelper.startPage(page, pageSize, false);
                List<WarehouseRegionPo> warehouseRegionPoList = warehouseRegionPoMapper.selectByRegionIdAndWarehouseIdOrderByWarehousePriority(regionList,warehouseIdList);
                if(warehouseRegionPoList.size() > 0){
                    ret = warehouseRegionPoList.stream()
                            .map(po -> this.getBo(po))
                            .collect(Collectors.toList());
                }
            }
        }
        return new PageInfo<>(ret);
    }

    /**
     * 用地区id查询可以配送的所有仓库(管理员)
     * -包括上级地区的仓库
     * - 在 mapper 层用连表查询实现了排序
     */
    public PageInfo<WarehouseRegion> retrieveAllByRegionIdAdmin(Long AdminShopId, Long id, Integer page, Integer pageSize) {
        List<WarehouseRegion> ret=new ArrayList<>();
        if(PLATFORM == AdminShopId && null != id){
            List<Long> regionList = regionDao.getParentRegionsById(id).getData().stream().map(SimpleRegion::getId).collect(Collectors.toList());
            regionList.add(id);
            PageHelper.startPage(page, pageSize, false);
            List<WarehouseRegionPo> warehouseRegionPoList = warehouseRegionPoMapper.selectByRegionIdOrderByWarehousePriority(regionList);
            if(warehouseRegionPoList.size() > 0){
                ret = warehouseRegionPoList.stream()
                        .map(po -> this.getBo(po))
                        .collect(Collectors.toList());
            }
        }
        return new PageInfo<>(ret);
    }



    /**
     * 新增仓库配送地区
     * @param bo
     * @param user
     * @return
     */
    public WarehouseRegion insert(WarehouseRegion bo, UserDto user,Long shopId) throws RuntimeException{
        WarehouseRegionPo po = getPo(bo);
        putUserFields(po, "creator",user);
        putGmtFields(po, "Create");
        warehouseRegionPoMapper.insert(po);
        clearRedis(po.getWarehouseId(),po.getRegionId(),shopId);
        return getBo(po);
    }

    /**
     * 查询单个物流配送地区
     * @param warehouseId
     * @param regionId
     * @return
     */
    public WarehouseRegion findByWarehouseIdAndRegionId(Long warehouseId,Long regionId){
        if(null != warehouseId && null !=regionId) {
            String key = String.format(WAREHOUSE_REGION_KEY, warehouseId, regionId);
            if (redisUtil.hasKey(key)) {
                return (WarehouseRegion) redisUtil.get(key);
            }
            WarehouseRegionPoExample example = new WarehouseRegionPoExample();
            WarehouseRegionPoExample.Criteria criteria = example.createCriteria();
            criteria.andWarehouseIdEqualTo(warehouseId);
            criteria.andRegionIdEqualTo(regionId);
            List<WarehouseRegionPo> warehouseRegionPoList = warehouseRegionPoMapper.selectByExample(example);
            if(warehouseRegionPoList.size() > 0){
                WarehouseRegion ret = this.getBo(warehouseRegionPoList.get(0));
                redisUtil.set(key, ret, timeout);
                return ret;
            }
        }
        return null;
    }

    /**
     * 更新仓库配送地区
     * @param bo
     * @param user
     * @return
     * @throws RuntimeException
     */
    public void save(WarehouseRegion bo, UserDto user,Long shopId) throws RuntimeException{
        WarehouseRegionPo po = getPo(bo);
        putUserFields(po, "modifier",user);
        putGmtFields(po, "Modified");
        WarehouseRegionPoExample example = new WarehouseRegionPoExample();
        WarehouseRegionPoExample.Criteria criteria = example.createCriteria();
        criteria.andWarehouseIdEqualTo(po.getWarehouseId());
        criteria.andRegionIdEqualTo(po.getRegionId());
        warehouseRegionPoMapper.updateByExampleSelective(po,example);
        clearRedis(po.getWarehouseId(),po.getRegionId(),shopId);
    }

    /**
     * 查询某个仓库的配送地区
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    public PageInfo<WarehouseRegion> retrieveByWarehouseId(Long id, Integer page, Integer pageSize) {
        List<WarehouseRegion> ret=new ArrayList<>();
        if(null != id){
            WarehouseRegionPoExample example = new WarehouseRegionPoExample();
            WarehouseRegionPoExample.Criteria criteria = example.createCriteria();
            criteria.andWarehouseIdEqualTo(id);
            PageHelper.startPage(page, pageSize, false);
            List<WarehouseRegionPo> warehouseRegionPoList = warehouseRegionPoMapper.selectByExample(example);
            if(warehouseRegionPoList.size() > 0){
                ret = warehouseRegionPoList.stream()
                        .map(po -> this.getBo(po))
                        .collect(Collectors.toList());
            }
        }
        return new PageInfo<>(ret);
    }

    /**
     * 查询某个仓库的配送地区
     * -内部处理逻辑调用的
     */
    public List<WarehouseRegion> retrieveByWarehouseIdInternal(Long id) {
        if(null != id){
            String key = String.format(WarehouseRegionDao.WAREHOUSE_REGION_LIST_INTERNAL_KEY, id);
            if(redisUtil.hasKey(key)){
                return (List<WarehouseRegion>) redisUtil.get(key);
            }
            WarehouseRegionPoExample example = new WarehouseRegionPoExample();
            WarehouseRegionPoExample.Criteria criteria = example.createCriteria();
            criteria.andWarehouseIdEqualTo(id);
            List<WarehouseRegionPo> warehouseRegionPoList = warehouseRegionPoMapper.selectByExample(example);
            if(warehouseRegionPoList.size() > 0){
                List<WarehouseRegion> ret = warehouseRegionPoList.stream()
                        .map(po -> this.getBo(po))
                        .collect(Collectors.toList());
                redisUtil.set(key, (Serializable) ret, timeout);
                return ret;
            }
        }
        return new ArrayList<>();
    }

    /**
     * 删除仓库配送地区
     */
    public int delete(WarehouseRegion bo,Long shopId) {
        int ret = 0;
        if(bo.getWarehouseId()!=null && bo.getRegionId()!=null){
            ret = warehouseRegionPoMapper.deleteByWarehouseIdAndRegionId(bo.getWarehouseId(),bo.getRegionId());
            clearRedis(bo.getWarehouseId(),bo.getRegionId(),shopId);
        }
        return ret;
    }

    /**
     * 用地区id查询可以配送的所有仓库(不分页的内部调用)
     * -包括上级地区的仓库
     *
     * @param shopId
     * @param id
     * @return
     */
    public List<WarehouseRegion> retrieveAllByRegionIdInternal(Long shopId, Long id) {
        if(null != shopId && null != id) {
            String key = String.format(WarehouseRegionDao.REGION_WAREHOUSE_LIST_INTERNAL_KEY, id, shopId);
            if (redisUtil.hasKey(key)) {
                return (List<WarehouseRegion>) redisUtil.get(key);
            }
            List<Long> warehouseIdList = warehouseDao.retrieveByShopIdInternal(shopId).stream().map(bo->bo.getId()).collect(Collectors.toList());
            if(warehouseIdList.size() > 0){
                List<Long> regionList = regionDao.getParentRegionsById(id).getData().stream().map(SimpleRegion::getId).collect(Collectors.toList());
                regionList.add(id);
                WarehouseRegionPoExample example = new WarehouseRegionPoExample();
                WarehouseRegionPoExample.Criteria criteria = example.createCriteria();
                criteria.andRegionIdIn(regionList);
                criteria.andWarehouseIdIn(warehouseIdList);
                List<WarehouseRegionPo> warehouseRegionPoList = warehouseRegionPoMapper.selectByExample(example);
                if(warehouseRegionPoList.size() > 0){
                    List<WarehouseRegion> ret = warehouseRegionPoList.stream()
                            .map(po -> this.getBo(po))
                            .collect(Collectors.toList());
                    redisUtil.set(key, (Serializable) ret, timeout);
                    return ret;
                }
            }
        }
        return new ArrayList<>();
    }
}

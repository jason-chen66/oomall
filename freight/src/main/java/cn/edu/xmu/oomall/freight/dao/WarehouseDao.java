package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.mapper.generator.WarehousePoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.WarehouseRegionPoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehousePo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehousePoExample;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehouseRegionPo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;
import static cn.edu.xmu.javaee.core.util.Common.*;

@Repository
public class WarehouseDao {

    private final static Logger logger = LoggerFactory.getLogger(WarehouseDao.class);
    public final static String WAREHOUSE_KEY = "FW%d";//%d为warehouseId
    public final static String SHOP_WAREHOUSE_LIST_INTERNAL_KEY = "FSWLI%d";//%d为shopId

    @Value("${oomall.freight.warehouse.timeout}")
    private int timeout;

    private WarehousePoMapper warehousePoMapper;

    private RegionDao regionDao;

    private WarehouseRegionDao warehouseRegionDao;

    private WarehouseLogisticsDao warehouseLogisticsDao;

    private RedisUtil redisUtil;

    @Autowired
    public WarehouseDao(WarehousePoMapper warehousePoMapper, RegionDao regionDao, WarehouseRegionDao warehouseRegionDao, WarehouseLogisticsDao warehouseLogisticsDao, RedisUtil redisUtil) {
        this.warehousePoMapper = warehousePoMapper;
        this.regionDao = regionDao;
        this.warehouseRegionDao = warehouseRegionDao;
        this.warehouseLogisticsDao = warehouseLogisticsDao;
        this.redisUtil = redisUtil;
    }

    private Warehouse getBo(WarehousePo po){
        Warehouse bo = Warehouse.builder().id(po.getId()).creatorId(po.getCreatorId()).creatorName(po.getCreatorName()).gmtCreate(po.getGmtCreate()).gmtModified(po.getGmtModified()).modifierId(po.getModifierId()).modifierName(po.getModifierName())
                .name(po.getName()).address(po.getAddress()).regionId(po.getRegionId()).senderName(po.getSenderName()).senderMobile(po.getSenderMobile())
                .shopId(po.getShopId()).priority(po.getPriority()).invalid(po.getInvalid()).build();
        this.setBo(bo);
        return bo;
    }

    private void setBo(Warehouse bo){
        bo.setRegionDao(regionDao);
        bo.setWarehouseRegionDao(warehouseRegionDao);
        bo.setWarehouseLogisticsDao(warehouseLogisticsDao);
    }

    private WarehousePo getPo(Warehouse bo){
        return  WarehousePo.builder().name(bo.getName()).address(bo.getAddress())
                .regionId(bo.getRegionId()).senderName(bo.getSenderName()).senderMobile(bo.getSenderMobile())
                .shopId(bo.getShopId()).priority(bo.getPriority()).invalid(bo.getInvalid()).build();
    }

    private void clearRedis(Long warehouseId, Long shopId){
        redisUtil.del(String.format(WAREHOUSE_KEY, warehouseId));
        redisUtil.del(String.format(SHOP_WAREHOUSE_LIST_INTERNAL_KEY, shopId));

    }

    public Warehouse insert(Warehouse bo, UserDto user) {
        WarehousePo po = getPo(bo);
        putUserFields(po, "creator",user);
        putGmtFields(po, "Create");
        warehousePoMapper.insert(po);
        clearRedis(po.getId(), po.getShopId());
        return getBo(po);
    }

    public PageInfo<Warehouse> retrieveByShopId(Long shopId, Integer page, Integer pageSize) {
        List<Warehouse> ret = new ArrayList<>();
        if(null != shopId) {
            WarehousePoExample warehousePoExample=new WarehousePoExample();
            WarehousePoExample.Criteria criteria=warehousePoExample.createCriteria();
            if(PLATFORM != shopId){
                //非平台管理员只获得本商铺的仓库列表
                criteria.andShopIdEqualTo(shopId);
            }
            warehousePoExample.setOrderByClause("priority");
            PageHelper.startPage(page,pageSize,false);
            List<WarehousePo> warehousePos = this.warehousePoMapper.selectByExample(warehousePoExample);
            if(null != warehousePos && warehousePos.size() > 0){
                ret=warehousePos.stream().map(po->getBo(po)).collect(Collectors.toList());
            }
        }
        return new PageInfo<>(ret);
    }

    public Warehouse findById(Long id) {
        if(null != id){
            String key = String.format(WAREHOUSE_KEY, id);
            if (redisUtil.hasKey(key)) {
                return (Warehouse) redisUtil.get(key);
            }
            WarehousePoExample warehousePoExample=new WarehousePoExample();
            WarehousePoExample.Criteria criteria=warehousePoExample.createCriteria();
            criteria.andIdEqualTo(id);
            List<WarehousePo> pos=warehousePoMapper.selectByExample(warehousePoExample);
            if(pos != null && pos.size() > 0){
                redisUtil.set(key, (Serializable) getBo(pos.get(0)),timeout);
                return getBo(pos.get(0));
            }
        }
        return null;
    }

    public void save(Warehouse bo, UserDto user) {
        WarehousePo po = getPo(bo);
        putUserFields(po, "modifier",user);
        putGmtFields(po, "Modified");
        warehousePoMapper.updateByPrimaryKeySelective(po);
        clearRedis(bo.getId(), bo.getShopId());
    }

    public void delete(Warehouse bo) {
        warehousePoMapper.deleteByPrimaryKey(bo.getId());
        clearRedis(bo.getId(), bo.getShopId());
    }


    /**
     * 用shopId查询仓库（内部调用）
     * @param shopId
     * @return
     */
    public List<Warehouse> retrieveByShopIdInternal(Long shopId) {
        if(null != shopId) {
            String key = String.format(SHOP_WAREHOUSE_LIST_INTERNAL_KEY, shopId);
            if (redisUtil.hasKey(key)) {
                return (List<Warehouse>) redisUtil.get(key);
            }
            WarehousePoExample warehousePoExample=new WarehousePoExample();
            WarehousePoExample.Criteria criteria=warehousePoExample.createCriteria();
            criteria.andShopIdEqualTo(shopId);
            List<WarehousePo> warehousePos=  this.warehousePoMapper.selectByExample(warehousePoExample);
            if(null != warehousePos && warehousePos.size() > 0){
                List<Warehouse> ret=warehousePos.stream().map(po->getBo(po)).collect(Collectors.toList());
                redisUtil.set(key, (Serializable) ret,timeout);
                return ret;
            }
        }
        return  new ArrayList<>();
    }
}

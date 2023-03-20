package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseLogistics;
import cn.edu.xmu.oomall.freight.mapper.generator.WarehouseLogisticsPoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.po.*;
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

import static cn.edu.xmu.javaee.core.util.Common.*;

@Repository
public class WarehouseLogisticsDao {

    private final static Logger logger = LoggerFactory.getLogger(WarehouseLogistics.class);

    public final static String WAREHOUSE_LOGISTICS_KEY = "FW%dL%d";//%d为仓库id和店铺物流id
    public final static String WAREHOUSE_LOGISTICS_LIST_INTERNAL_KEY = "FWLLI%d";//%d为仓库id
    @Value("${oomall.freight.warehouse-logistics.timeout}")
    private int timeout;

    private WarehouseLogisticsPoMapper warehouseLogisticsPoMapper;

    private WarehouseDao warehouseDao;

    private ShopLogisticsDao shopLogisticsDao;

    private RedisUtil redisUtil;

    @Autowired
    @Lazy
    public WarehouseLogisticsDao(WarehouseLogisticsPoMapper warehouseLogisticsPoMapper, WarehouseDao warehouseDao, ShopLogisticsDao shopLogisticsDao, RedisUtil redisUtil) {
        this.warehouseLogisticsPoMapper = warehouseLogisticsPoMapper;
        this.warehouseDao = warehouseDao;
        this.shopLogisticsDao = shopLogisticsDao;
        this.redisUtil = redisUtil;
    }

    private WarehouseLogistics getBo(WarehouseLogisticsPo po){
        WarehouseLogistics bo = WarehouseLogistics.builder().id(po.getId()).creatorId(po.getCreatorId()).creatorName(po.getCreatorName()).gmtCreate(po.getGmtCreate()).gmtModified(po.getGmtModified()).modifierId(po.getModifierId()).modifierName(po.getModifierName())
                .warehouseId(po.getWarehouseId()).shopLogisticsId(po.getShopLogisticsId()).beginTime(po.getBeginTime()).endTime(po.getEndTime()).invalid(po.getInvalid()).build();
        this.setBo(bo);
        return bo;
    }

   private void setBo(WarehouseLogistics bo) {
        bo.setWarehouseDao(warehouseDao);
        bo.setShopLogisticsDao(shopLogisticsDao);
    }

    private WarehouseLogisticsPo getPo(WarehouseLogistics bo){
        return  WarehouseLogisticsPo.builder().warehouseId(bo.getWarehouseId()).shopLogisticsId(bo.getShopLogisticsId())
                .beginTime(bo.getBeginTime()).endTime(bo.getEndTime()).invalid(bo.getInvalid()).build();
    }

    private void clearRedis(Long warehouseId, Long shopLogisticsId){
        redisUtil.del(String.format(WAREHOUSE_LOGISTICS_KEY, warehouseId, shopLogisticsId));
        redisUtil.del(String.format(WAREHOUSE_LOGISTICS_LIST_INTERNAL_KEY, warehouseId));
    }

    public WarehouseLogistics findByWarehouseIdAndShopLogisticsId(Long warehouseId, Long shopLogisticsId) {
        if (null != warehouseId && null != shopLogisticsId) {
            String key = String.format(WAREHOUSE_LOGISTICS_KEY, warehouseId, shopLogisticsId);
            if (redisUtil.hasKey(key)) {
                return (WarehouseLogistics) redisUtil.get(key);
            }
            WarehouseLogisticsPoExample example = new WarehouseLogisticsPoExample();
            WarehouseLogisticsPoExample.Criteria criteria = example.createCriteria();
            criteria.andWarehouseIdEqualTo(warehouseId);
            criteria.andShopLogisticsIdEqualTo(shopLogisticsId);
            List<WarehouseLogisticsPo> pos = warehouseLogisticsPoMapper.selectByExample(example);
            if (pos != null && pos.size() > 0) {
                WarehouseLogistics ret = getBo(pos.get(0));
                redisUtil.set(key, ret, timeout);
                return ret;
            }
        }
        return null;
    }

    public WarehouseLogistics insert(WarehouseLogistics bo, UserDto user) {
        WarehouseLogisticsPo po = getPo(bo);
        putUserFields(po, "creator",user);
        putGmtFields(po, "Create");
        warehouseLogisticsPoMapper.insert(po);
        clearRedis(bo.getWarehouseId(), bo.getShopLogisticsId());
        return getBo(po);
    }

    public void save(WarehouseLogistics bo, UserDto user) {
        WarehouseLogisticsPo po = getPo(bo);
        putUserFields(po, "modifier",user);
        putGmtFields(po, "Modified");
        WarehouseLogisticsPoExample example = new WarehouseLogisticsPoExample();
        WarehouseLogisticsPoExample.Criteria criteria = example.createCriteria();
        criteria.andWarehouseIdEqualTo(po.getWarehouseId());
        criteria.andShopLogisticsIdEqualTo(po.getShopLogisticsId());
        warehouseLogisticsPoMapper.updateByExampleSelective(po,example);
        clearRedis(bo.getWarehouseId(), bo.getShopLogisticsId());
    }

    public void delete(WarehouseLogistics bo) {
        warehouseLogisticsPoMapper.deleteByWarehouseIdAndShopLogisticsId(bo.getWarehouseId(),bo.getShopLogisticsId());
        clearRedis(bo.getWarehouseId(), bo.getShopLogisticsId());
    }

    public PageInfo<WarehouseLogistics> retrieveByWarehouseId(Long warehouseId, Integer page, Integer pageSize) {
        List<WarehouseLogistics> ret = new ArrayList<>();
        if(null != warehouseId) {
            WarehouseLogisticsPoExample warehouseLogisticsPoExample=new WarehouseLogisticsPoExample();
            WarehouseLogisticsPoExample.Criteria criteria=warehouseLogisticsPoExample.createCriteria();
            criteria.andWarehouseIdEqualTo(warehouseId);
            PageHelper.startPage(page,pageSize,false);
            List<WarehouseLogisticsPo> warehouseLogisticsPos=  this.warehouseLogisticsPoMapper.selectByExample(warehouseLogisticsPoExample);
            if(null!=warehouseLogisticsPos && warehouseLogisticsPos.size()>0){
                ret=warehouseLogisticsPos.stream().map(po->getBo(po)).collect(Collectors.toList());
            }
        }
        return new PageInfo<>(ret);
    }

    public List<WarehouseLogistics> retrieveByWarehouseIdInternal(Long warehouseId){
        if(null != warehouseId) {
            String key = String.format(WAREHOUSE_LOGISTICS_LIST_INTERNAL_KEY, warehouseId);
            if(redisUtil.hasKey(key)) {
                return (List<WarehouseLogistics>) redisUtil.get(key);
            }
            WarehouseLogisticsPoExample warehouseLogisticsPoExample=new WarehouseLogisticsPoExample();
            WarehouseLogisticsPoExample.Criteria criteria=warehouseLogisticsPoExample.createCriteria();
            criteria.andWarehouseIdEqualTo(warehouseId);
            List<WarehouseLogisticsPo> warehouseLogisticsPos=  this.warehouseLogisticsPoMapper.selectByExample(warehouseLogisticsPoExample);
            if(null!=warehouseLogisticsPos && warehouseLogisticsPos.size()>0){
                List<WarehouseLogistics> ret=warehouseLogisticsPos.stream().map(po->getBo(po)).collect(Collectors.toList());
                redisUtil.set(key, (Serializable) ret,timeout);
                return ret;
            }
        }
        return new ArrayList<>();
    }

}

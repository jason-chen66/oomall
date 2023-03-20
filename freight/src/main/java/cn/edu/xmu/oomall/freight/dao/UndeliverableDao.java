package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.dao.bo.Undeliverable;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.SimpleRegion;
import cn.edu.xmu.oomall.freight.mapper.generator.UndeliverablePoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.po.UndeliverablePo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.UndeliverablePoExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.*;

/**
 * 商铺物流渠道
 *
 * @author Dasong Lu
 */
@Repository
public class UndeliverableDao {

    private static final Logger logger = LoggerFactory.getLogger(ShopLogisticsDao.class);


    public static final String SHOP_LOGISTICS_KEY = "SL%d";


    @Value("${oomall.freight.shop-logistics.timeout}")
    private long timeout;

    private UndeliverablePoMapper undeliverableRegionMapper;

    private RedisUtil redisUtil;

    private RegionDao regionDao;

    private ShopLogisticsDao shopLogisticsDao;

    @Autowired
    public UndeliverableDao(UndeliverablePoMapper undeliverableRegionMapper, RedisUtil redisUtil, RegionDao regionDao,ShopLogisticsDao shopLogisticsDao) {
        this.undeliverableRegionMapper = undeliverableRegionMapper;
        this.redisUtil = redisUtil;
        this.regionDao=regionDao;
        this.shopLogisticsDao=shopLogisticsDao;
    }

    /**
     * 获得po对象
     * @param bo
     * @return
     */
    private UndeliverablePo getPo(Undeliverable bo) {
        return UndeliverablePo.builder().shopLogisticsId(bo.getShopLogisticsId()).regionId(bo.getRegionId())
                .beginTime(bo.getBeginTime()).endTime(bo.getEndTime()).build();
    }

    /**
     * 获得bo对象
     * @param po
     * @return
     */
    private Undeliverable getBo(UndeliverablePo po) {
        Undeliverable bo=Undeliverable.builder().id(po.getId()).regionId(po.getRegionId()).shopLogisticsId(po.getShopLogisticsId())
                .beginTime(po.getBeginTime()).endTime(po.getEndTime()).build();
        this.setBo(bo);
        return bo;
    }

    private void setBo(Undeliverable bo){
        bo.setRegionDao(this.regionDao);
        bo.setShopLogisticsDao(this.shopLogisticsDao);
    }

    /**
     * 查询某个不可达地区
     * @param lid
     * @param rid
     * @return
     */
    public Undeliverable findByShopLogisticsIdAndRegionId(Long lid, Long rid) {
        UndeliverablePoExample example=new UndeliverablePoExample();
        UndeliverablePoExample.Criteria criteria= example.createCriteria();
        criteria.andRegionIdEqualTo(rid);
        criteria.andShopLogisticsIdEqualTo(lid);
        List<UndeliverablePo> undeliverablePos=undeliverableRegionMapper.selectByExample(example);
        if(undeliverablePos.size()>0)
            return getBo(undeliverablePos.get(0));
        return null;
    }

    /**
     * 商户指定快递公司无法配送某个地区
     */
    public Undeliverable insert(Undeliverable undeliverable, UserDto user) {
        UndeliverablePo po =getPo(undeliverable);
        putUserFields(po,"creator",user);
        putGmtFields(po,"Create");
        undeliverableRegionMapper.insert(po);
        String key = String.format(UndeliverableDao.SHOP_LOGISTICS_KEY, po.getShopLogisticsId());
        redisUtil.del(key);
        return getBo(po);
    }

    /**
     * 商户更新不可达信息
     */
    public ReturnObject save(Undeliverable undeliverable, UserDto user) {
        UndeliverablePo po = getPo(undeliverable);
        putUserFields(po,"modifier",user);
        putGmtFields(po,"Modified");
        UndeliverablePoExample example=new UndeliverablePoExample();
        UndeliverablePoExample.Criteria criteria=example.createCriteria();
        criteria.andShopLogisticsIdEqualTo(undeliverable.getShopLogisticsId());
        criteria.andRegionIdEqualTo(undeliverable.getRegionId());
        int ret=undeliverableRegionMapper.updateByExampleSelective(po,example);
//        if(ret==0)
//            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR);
        String key = String.format(UndeliverableDao.SHOP_LOGISTICS_KEY, undeliverable.getShopLogisticsId());
        redisUtil.del(key);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商户查询快递公司无法配送的地区
     */
    public PageInfo<Undeliverable> retrieveByShopLogisticsId(Long lid, Integer page, Integer pageSize) {
        List<Undeliverable> ret=null;
        String key = String.format(UndeliverableDao.SHOP_LOGISTICS_KEY, lid);
        if(redisUtil.hasKey(key)){
            ret = (List<Undeliverable>) redisUtil.get(key);
            return new PageInfo<>(ret);
        }

        UndeliverablePoExample example=new UndeliverablePoExample();
        UndeliverablePoExample.Criteria criteria= example.createCriteria();
        criteria.andShopLogisticsIdEqualTo(lid);
        PageHelper.startPage(page, pageSize, false);
        List<UndeliverablePo> undeliverablePoList=undeliverableRegionMapper.selectByExample(example);
        if(null!=undeliverablePoList && undeliverablePoList.size()>0){
            List<Undeliverable> undeliverableList=undeliverablePoList.stream()
                    .map(po->this.getBo(po))
                    .collect(Collectors.toList());
            redisUtil.set(key, (Serializable) undeliverableList, timeout);
            return new PageInfo<>(undeliverableList);
        }
        else
            return null;
    }

    public ReturnObject deleteByShopLogisticsIdAndRegionId(Long lid, Long rid) {
        int ret=undeliverableRegionMapper.deleteByShopLogisticsIdAndRegionId(lid,rid);
//        if(ret==0)
//            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR);
        String key = String.format(UndeliverableDao.SHOP_LOGISTICS_KEY, lid);
        redisUtil.del(key);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 获取该地区ID的所有不可达 (没分页)
     * - 包括上级地区的不可达
     */
    public List<Undeliverable> retrieveByRegionIdInternal(Long regionId){
        if(null != regionId)
        {
            List<Long> regionList = regionDao.getParentRegionsById(regionId).getData().stream().map(SimpleRegion::getId).collect(Collectors.toList());
            regionList.add(regionId);
            UndeliverablePoExample example=new UndeliverablePoExample();
            UndeliverablePoExample.Criteria criteria= example.createCriteria();
            criteria.andRegionIdIn(regionList);
            List<UndeliverablePo> undeliverablePoList=undeliverableRegionMapper.selectByExample(example);
            if(undeliverablePoList.size()>0){
                List<Undeliverable> undeliverableList=undeliverablePoList.stream()
                        .map(po->this.getBo(po))
                        .collect(Collectors.toList());
                return undeliverableList;
            }
        }
        return new ArrayList<>();
    }
}

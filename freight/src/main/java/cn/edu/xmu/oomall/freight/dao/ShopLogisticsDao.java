//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.mapper.generator.ShopLogisticsPoMapper;
import cn.edu.xmu.oomall.freight.mapper.generator.po.ShopLogisticsPo;
import cn.edu.xmu.oomall.freight.mapper.generator.po.ShopLogisticsPoExample;
import cn.edu.xmu.oomall.freight.mapper.generator.po.WarehousePo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.io.Serializable;
import java.util.*;

import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.*;

/**
 * 商铺物流渠道
 */
@Repository
public class ShopLogisticsDao {

    private static final Logger logger = LoggerFactory.getLogger(ShopLogisticsDao.class);

    //存入的为同一个商铺的所有商铺物流列表,ShopLogisticsList,为%d为shopId
    public static final String LIST_KEY = "SL%d";

    //存入的为单个商铺物流,ShopLogistics,为%d为shopLogisticsId
    public static final String KEY = "S%d";

    @Value("${oomall.freight.shop-logistics.timeout}")
    private long timeout;

    private final ShopLogisticsPoMapper shopLogisticsPoMapper;

    private final RedisUtil redisUtil;

    private final LogisticsDao LogisticsDao;

    @Autowired
    public ShopLogisticsDao(ShopLogisticsPoMapper shopLogisticsPoMapper, RedisUtil redisUtil, LogisticsDao LogisticsDao) {
        this.shopLogisticsPoMapper = shopLogisticsPoMapper;
        this.redisUtil = redisUtil;
        this.LogisticsDao = LogisticsDao;
    }

    private ShopLogistics getBo(ShopLogisticsPo po, Optional<String> redisKey) {
        logger.debug("PoInfo = {}",po.getId());
        ShopLogistics ret = ShopLogistics.builder().id(po.getId()).creatorId(po.getCreatorId()).creatorName(po.getCreatorName()).gmtCreate(po.getGmtCreate()).gmtModified(po.getGmtModified()).modifierId(po.getModifierId()).modifierName(po.getModifierName())
                .shopId(po.getShopId()).logisticsId(po.getLogisticsId()).invalid(po.getInvalid())
                .secret(po.getSecret()).priority(po.getPriority()).build();
        logger.debug("RetInfo = {}",ret.getId());
        redisKey.ifPresent(key -> redisUtil.set(key, ret, timeout));
        this.setBo(ret);
        return ret;
    }

    private void setBo(ShopLogistics bo) {
        bo.setLogisticsDao(this.LogisticsDao);
    }

    private ShopLogisticsPo getPo(ShopLogistics bo){
        return  ShopLogisticsPo.builder().shopId(bo.getShopId())
                .logisticsId(bo.getLogisticsId())
                .secret(bo.getSecret())
                .invalid(bo.getInvalid())
                .priority(bo.getPriority()).build();
    }

    private void clearRedis(Long shopLogisticsId, Long shopId) {
        redisUtil.del(String.format(KEY, shopLogisticsId));
        redisUtil.del(String.format(LIST_KEY, shopId));
    }

    /**
     * 通过 id 查找商铺物流渠道
     *
     * @param id 商铺物流渠道 id
     * @return 商铺物流渠道
     * @throws RuntimeException 未找到商铺物流渠道
     */
    public ShopLogistics findById(Long id) throws RuntimeException {
        ShopLogistics ret = null;
        if (null != id) {
            String key = String.format(KEY, id);
            if (redisUtil.hasKey(key)) {
                ret = (ShopLogistics) redisUtil.get(key);
                this.setBo(ret);
            } else {
                ShopLogisticsPo po = shopLogisticsPoMapper.selectByPrimaryKey(id);
                ret = this.getBo(po, Optional.of(key));
            }
        }
        logger.debug("findObjById: id = " + id + " ret = " + ret);
        return ret;
    }

    /**
     * 通过 shopId 和 logisticsId 查找商铺物流渠道
     *
     * @param shopId      商铺 id
     * @param LogisticsId 物流渠道 id
     * @return 商铺物流渠道
     * @throws RuntimeException 未找到商铺物流渠道
     */
    public ShopLogistics findByShopIdAndLogisticsId(Long shopId, Long LogisticsId) throws RuntimeException {
        ShopLogistics ret = null;
        if (null != shopId && null != LogisticsId) {
            ShopLogisticsPoExample example = new ShopLogisticsPoExample();
            ShopLogisticsPoExample.Criteria criteria = example.createCriteria();
            criteria.andLogisticsIdEqualTo(LogisticsId);
            criteria.andShopIdEqualTo(shopId);
            List<ShopLogisticsPo> poList = this.shopLogisticsPoMapper.selectByExample(example);

            if (poList.size() > 0) {
                ShopLogisticsPo temp=poList.get(0);
                ret = getBo(temp, Optional.ofNullable(null));
                ret.setLogisticsDao(this.LogisticsDao);
            } else {
                return ret;
            }
        }
        return ret;
    }

    /**
     * 搜索商铺所有物流渠道（分页查询）
     *
     * @param shopId   商铺 id
     * @param page     页码
     * @param pageSize 页大小
     * @return 商铺物流渠道列表
     * @throws RuntimeException 未找到商铺物流渠道
     */
    public PageInfo<ShopLogistics> retrieveByShopId(Long shopId, Integer page, Integer pageSize) throws RuntimeException {
        List<ShopLogistics> ret = null;
        String key = String.format(LIST_KEY, shopId);
        if (redisUtil.hasKey(key)) {
            ret = (List<ShopLogistics>) redisUtil.get(key);
            return new PageInfo<>(ret);
        }
        ShopLogisticsPoExample shopLogisticsPoExample = new ShopLogisticsPoExample();
        ShopLogisticsPoExample.Criteria criteria = shopLogisticsPoExample.createCriteria();
        criteria.andShopIdEqualTo(shopId);
        PageHelper.startPage(page, pageSize, false);
        List<ShopLogisticsPo> shopLogisticsPoList = shopLogisticsPoMapper.selectByExample(shopLogisticsPoExample);

        if (shopLogisticsPoList.size() > 0) {
            ret = shopLogisticsPoList
                    .stream()
                    .map(po -> this.getBo(po, Optional.ofNullable(null)))
                    .collect(Collectors.toList());
            redisUtil.set(key, (Serializable) ret,timeout);
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        }

        return new PageInfo<>(ret);
    }

    /**
     * 搜索商铺所有物流渠道（没有分页）
     *
     * @param shopId   商铺 id
     * @return 商铺物流渠道列表
     * @throws RuntimeException 未找到商铺物流渠道
     */
    public List<ShopLogistics> retrieveByShopIdInternal(Long shopId) throws RuntimeException {
        List<ShopLogistics> ret = null;
        ShopLogisticsPoExample shopLogisticsPoExample = new ShopLogisticsPoExample();
        ShopLogisticsPoExample.Criteria criteria = shopLogisticsPoExample.createCriteria();
        criteria.andShopIdEqualTo(shopId);
        List<ShopLogisticsPo> shopLogisticsPoList = shopLogisticsPoMapper.selectByExample(shopLogisticsPoExample);
        if (shopLogisticsPoList.size() > 0) {
            ret = shopLogisticsPoList
                    .stream()
                    .map(po -> this.getBo(po,Optional.ofNullable(null)))
                    .collect(Collectors.toList());
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,String.format("商铺(id=%d)的商铺物流不存在",shopId));
        }
        return ret;
    }

    /**
     * 商家签约物流渠道
     *
     * @param bo   需要更新的商铺物流渠道信息
     * @param user 修改人
     * @return 修改后的商铺物流渠道信息
     */
    public ShopLogistics save(ShopLogistics bo, UserDto user) {
        ShopLogisticsPo shopLogisticsPo = getPo(bo);
        putUserFields(shopLogisticsPo, "modifier", user);
        putGmtFields(shopLogisticsPo, "Modified");
        ShopLogisticsPoExample example=new ShopLogisticsPoExample();
        ShopLogisticsPoExample.Criteria criteria=example.createCriteria();
        criteria.andShopIdEqualTo(bo.getShopId());
        criteria.andLogisticsIdEqualTo(bo.getLogisticsId());
        int ret=shopLogisticsPoMapper.updateByExampleSelective(shopLogisticsPo,example);
//        if(ret==0)
//            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR);
        clearRedis(shopLogisticsPo.getId(), shopLogisticsPo.getShopId());
        return getBo(shopLogisticsPo, Optional.ofNullable(null));
    }

    public ShopLogistics insert(ShopLogistics bo, UserDto user) {
        ShopLogisticsPo po=getPo(bo);
        putUserFields(po, "creator",user);
        putGmtFields(po, "Create");
        int ret=shopLogisticsPoMapper.insert(po);
//        if(ret==0)
//            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR);
        clearRedis(po.getId(), po.getShopId());
        return getBo(po,Optional.ofNullable(null));
    }


}

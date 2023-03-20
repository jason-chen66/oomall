package cn.edu.xmu.oomall.order.dao.openfeign.proxy;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.order.dao.openfeign.ShopDao;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.FreightDto;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.ShopDto;
import cn.edu.xmu.oomall.order.dao.openfeign.vo.FreightVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component("shopDaoProxy")
public class ShopDaoProxy implements ShopDao {

    @Autowired
    public ShopDaoProxy(@Qualifier("shopDao") ShopDao shopDao, RedisUtil redisUtil) {
        this.shopDao = shopDao;
        this.redisUtil = redisUtil;
    }

    @Override
    public InternalReturnObject<ShopDto> findShop(Long id) {
        if (0 == shopTimeout) {
            return shopDao.findShop(id);
        }
        String key = String.format(SHOP_DTO_KEY, id);
        if (redisUtil.hasKey(key)) {
            return new InternalReturnObject<>((ShopDto) redisUtil.get(key));
        }
        InternalReturnObject<ShopDto> ret = shopDao.findShop(id);
        if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())) {
            redisUtil.set(key, (Serializable) ret.getData(), shopTimeout);
        }
        return ret;
    }

    @Override
    public InternalReturnObject<FreightDto> getFreightPrice(Long id, Long rid, List<FreightVo> freightVoList) {
        return shopDao.getFreightPrice(id, rid, freightVoList);
    }

    @Value("${oomall.order.open-feign-timeout.shop-dao.shop-dto}")
    private static Long shopTimeout;

    private static String SHOP_DTO_KEY = "SPD%d";

    private final ShopDao shopDao;

    private final RedisUtil redisUtil;

}

package cn.edu.xmu.oomall.order.dao.openfeign.proxy;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.order.dao.openfeign.GoodsDao;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.*;
import cn.edu.xmu.oomall.order.dao.openfeign.vo.DiscountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component("goodsDaoProxy")
public class GoodsDaoProxy implements GoodsDao {


    @Autowired
    public GoodsDaoProxy(@Qualifier("goodsDao") GoodsDao goodsDao, RedisUtil redisUtil) {
        this.goodsDao = goodsDao;
        this.redisUtil = redisUtil;
    }

    @Override
    public InternalReturnObject<OnsaleDto> getOnsaleById(Long shopId, Long id) {
        if (0 == onsaleTimeout) {
            return goodsDao.getOnsaleById(shopId, id);
        }
        String key = String.format(ONSALE_DTO_KEY, shopId, id);
        if (redisUtil.hasKey(key)) {
            return new InternalReturnObject<>((OnsaleDto) redisUtil.get(key));
        }
        InternalReturnObject<OnsaleDto> ret = goodsDao.getOnsaleById(shopId, id);
        if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())) {
            redisUtil.set(key, (Serializable) ret.getData(), onsaleTimeout);
        }
        return ret;
    }

    @Override
    public InternalReturnObject<List<DiscountDto>> calculateDiscount(Long id, List<DiscountVo> discountVo) {
        return goodsDao.calculateDiscount(id, discountVo);
    }

    @Override
    public InternalReturnObject<CouponActivityDto> getCouponActivityById(Long shopId, Long id) {
        return goodsDao.getCouponActivityById(shopId, id);
    }

    @Override
    public InternalReturnObject<ProductDto> getProductByIdAndShopId(Long shopId, Long id) {
        if (0 == productTimeout) {
            return goodsDao.getProductByIdAndShopId(shopId, id);
        }
        String key = String.format(PRODUCT_DTO_KEY, shopId, id);
        if (redisUtil.hasKey(key)) {
            return new InternalReturnObject<>((ProductDto) redisUtil.get(key));
        }
        InternalReturnObject<ProductDto> ret = goodsDao.getProductByIdAndShopId(shopId, id);
        if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())) {
            redisUtil.set(key, (Serializable) ret.getData(), onsaleTimeout);
        }
        return ret;
    }

    @Value("${oomall.order.open-feign-timeout.goods-dao.onsale-dto}")
    private static Long onsaleTimeout;

    private static String ONSALE_DTO_KEY = "OSD%d%d";

    private static String TEMPLATE_DTO_KEY = "TPT%d%d";

    @Value("${oomall.order.open-feign-timeout.goods-dao.product-dto}")
    private static Long productTimeout;

    private static String PRODUCT_DTO_KEY = "PDT%d%d";

    private final GoodsDao goodsDao;

    private final RedisUtil redisUtil;
}

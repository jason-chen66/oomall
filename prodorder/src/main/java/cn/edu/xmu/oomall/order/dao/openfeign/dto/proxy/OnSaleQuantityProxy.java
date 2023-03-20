package cn.edu.xmu.oomall.order.dao.openfeign.dto.proxy;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import cn.edu.xmu.oomall.order.dao.openfeign.GoodsDao;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.OnsaleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

public class OnSaleQuantityProxy extends OnsaleDto {

    public OnSaleQuantityProxy(Long onSaleId, RedisUtil redisUtil, GoodsDao goodsDao) {
        super();
        setId(onSaleId);
        this.redisUtil = redisUtil;
        this.goodsDao = goodsDao;
        isLoaded = false;
    }

    public Integer getMaxQuantity() {
        String key = String.format(STOCK_KEY, getId());
        if (redisUtil.hasKey(key)) {
            return (Integer) redisUtil.get(key);
        } else {
            initializeFromOpenFeign();
            redisUtil.set(key, maxQuantityProxy, genTimeout(getEndTime()));
            return maxQuantityProxy;
        }
    }

    public Integer getQuantity() {
        String key = String.format(MAX_QUANTITY_KEY, getId());
        if (redisUtil.hasKey(key)) {
            return (Integer) redisUtil.get(key);
        } else {
            initializeFromOpenFeign();
            redisUtil.set(key, quantityProxy, genTimeout(getEndTime()));
            return quantityProxy;
        }
    }

    public void descQuantity(Integer quantity) {
        String key = String.format(STOCK_KEY, getId());
        redisUtil.decr(key, quantity);
    }

    public void initializeFromOpenFeign() {
        if (isLoaded) {
            return;
        }
        InternalReturnObject<OnsaleDto> ret = goodsDao.getOnsaleById(PLATFORM, getId());
        if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())) {
            OnsaleDto onsaleDto = ret.getData();
            setShop(onsaleDto.getShop());
            setProduct(onsaleDto.getProduct());
            setPrice(onsaleDto.getPrice());
            setBeginTime(onsaleDto.getBeginTime());
            setEndTime(onsaleDto.getEndTime());
            setQuantity(onsaleDto.getQuantity());
            setMaxQuantity(onsaleDto.getMaxQuantity());
            setType(onsaleDto.getType());
            setActList(onsaleDto.getActList());
            quantityProxy = onsaleDto.getQuantity();
            maxQuantityProxy = onsaleDto.getMaxQuantity();
            isLoaded = true;
        } else {
            logger.debug("getOnsale: " + ret.getErrmsg());
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "销售", getId()));
        }
    }

    public OrderItem parseToBo() {
        initializeFromOpenFeign();
        return OrderItem.builder()
                .quantity(quantityProxy)
                .onsaleId(getId())
                .productId(getProduct().getId())
                .price(getPrice() * getQuantity())
                .name(getProduct().getName())
                .build();
    }

    private static Long genTimeout(LocalDateTime endTime) {
        return Duration.between(LocalDateTime.now(), endTime).toSeconds();
    }

    private Integer quantityProxy = 0;

    private Integer maxQuantityProxy = 0;

    private Boolean isLoaded;

    private final String STOCK_KEY = "STK%d";

    private final String MAX_QUANTITY_KEY = "MQTT%d";

    private static final Logger logger = LoggerFactory.getLogger(OnSaleQuantityProxy.class);

    private final RedisUtil redisUtil;

    private final GoodsDao goodsDao;
}

//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.order.dao.OrderItemDao;
import cn.edu.xmu.oomall.order.dao.openfeign.CustomerDao;
import cn.edu.xmu.oomall.order.dao.openfeign.GoodsDao;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.*;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.OrderItemActivityDto;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.OrderItemCouponDto;
import cn.edu.xmu.oomall.order.dao.openfeign.vo.DiscountVo;
import cn.edu.xmu.oomall.order.service.dto.SimpleCouponDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItem extends OOMallObject implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(OrderItem.class);

    @Builder
    public OrderItem(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified, Long orderId, Long onsaleId, Integer quantity, Long price, Long discountPrice, Long point, String name, Long actId, Long couponId, Long productId, Byte commented) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.orderId = orderId;
        this.onsaleId = onsaleId;
        this.quantity = quantity;
        this.price = price;
        this.discountPrice = discountPrice;
        this.point = point;
        this.name = name;
        this.actId = actId;
        this.couponId = couponId;
        this.productId = productId;
        this.commented = commented;
    }
    @JsonIgnore
    @Setter
    private GoodsDao goodsDao;

    @JsonIgnore
    @ToString.Exclude
    private CustomerDao customerDao;

    @Setter
    @Getter
    private Long orderId;

    @Setter
    @Getter
    private Long onsaleId;

    @Setter
    @Getter
    private Long productId;


    @Getter
    @Setter
    private Integer quantity;

    @Setter
    @Getter
    private Long price;

    @Setter
    @Getter
    private Long discountPrice;

    @Setter
    @Getter
    private Long point;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private Long actId;

    @Setter
    OrderItemActivityDto activity;

    @Setter
    @Getter
    private Long couponId;

    @Setter
    @Getter
    OrderItemCouponDto coupon;

    @Setter
    @Getter
    Byte commented;



    /**
     * 计算商品明细的佣金
     *
     * @param shopId
     * @return
     * @throws BusinessException
     */
    public Long getDivAmountPerItem(Long shopId) throws BusinessException {
        OnsaleDto onsaleDto = goodsDao.getOnsaleById(shopId,this.getOnsaleId()).getData();
        ProductDto dto = goodsDao.getProductByIdAndShopId(shopId, onsaleDto.getProduct().getId()).getData();
        double r = dto.getCommissionRatio() / 100.0;
        Double ret =  (this.price - this.discountPrice - this.point / 100) * r;
        logger.debug("OrderItem:getDivAmountPerItem={},onsaleId={}",ret,this.onsaleId);
        return ret.longValue();
    }

    public Long calPayPrice() throws BusinessException {
        if (this.price != null) {
            return this.price - this.discountPrice;
        } else return 0L;
    }


    public OrderItemActivityDto getActivity() throws BusinessException{
        if(this.actId!=null){
            OnsaleDto onsaleDto = goodsDao.getOnsaleById(0L,this.onsaleId).getData();
            if(onsaleDto!=null){
                OrderItemActivityDto ret = new OrderItemActivityDto();
                IdNameTypeDto dto = onsaleDto.getActList().stream().filter(act->act.getId().equals(this.actId)).findAny().orElse(null);
                if(dto!=null){
                    ret.setId(dto.getId());
                    ret.setName(dto.getName());
                    ret.setType(dto.getType());
                }
                this.activity = ret;
                return ret;
            }
            else throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(),"活动",this.actId));
        }
        throw new BusinessException(ReturnNo.PARAMETER_MISSED,ReturnNo.PARAMETER_MISSED.getMessage());
    }

    public SimpleCouponDto findCouponAndActivity() {
        if(this.couponId!=null){
            CouponDto dto = customerDao.findCouponById(0L,this.couponId).getData();
            if(dto!=null){
                OrderItemCouponDto ret = new OrderItemCouponDto();
                ret.setId(dto.getId());
                ret.setName(dto.getName());
                ret.setCouponSn(dto.getCouponSn());
                if(this.actId!=null){
                    CouponActivityDto actDto = goodsDao.getCouponActivityById(0L,this.actId).getData();
                    if(actDto!=null){
                        CouponActivityDto actRet = new CouponActivityDto();
                        actRet.setId(actDto.getId());
                        actRet.setName(actDto.getName());
                        actRet.setQuantity(actDto.getQuantity());
                        actRet.setCouponTime(actDto.getCouponTime());
                        ret.setActivity(actRet);
                        this.coupon = ret;
                        return ret;
                    }
                    else throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(),"活动",this.actId));
                }
                throw new BusinessException(ReturnNo.PARAMETER_MISSED,ReturnNo.PARAMETER_MISSED.getMessage());
            }
            else throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(),"活动",this.actId));
        }
        throw new BusinessException(ReturnNo.PARAMETER_MISSED,ReturnNo.PARAMETER_MISSED.getMessage());
    }
}

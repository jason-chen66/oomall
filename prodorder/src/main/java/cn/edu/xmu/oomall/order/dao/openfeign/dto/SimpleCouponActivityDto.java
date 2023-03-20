package cn.edu.xmu.oomall.order.dao.openfeign.dto;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.order.dao.openfeign.GoodsDao;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleCouponActivityDto {

    private Long id;

    /**
     * 优惠活动名称
     */
    private String name;

    /**
     * 优惠券数目，0-不用优惠券
     */
    private Integer quantity;

    /**
     * 开始领优惠卷时间
     */
    private LocalDateTime couponTime;
}

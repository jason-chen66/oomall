//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.*;
import cn.edu.xmu.oomall.order.dao.openfeign.vo.DiscountVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "goods-service", qualifier = "goodsDao")
public interface GoodsDao {

    /**
     * 查询销售详情
     *
     * @param shopId
     * @param id
     * @return
     */
    @GetMapping("/shops/{shopId}/onsales/{id}")
    InternalReturnObject<OnsaleDto> getOnsaleById(@PathVariable Long shopId, @PathVariable Long id);

    /**
     * 计算优惠
     *
     * @param id         活动 id
     * @param discountVo onsale 信息
     */
    @PostMapping("/couponactivities/{id}/caculate")
    InternalReturnObject <List<DiscountDto>> calculateDiscount(@PathVariable Long id, @RequestBody List<DiscountVo> discountVo);

    /**
     * 查看优惠活动详情
     *
     * @param shopId 商店 id
     * @param id     活动 id
     */
    @GetMapping("/shops/{shopId}/couponactivities/{id}")
    InternalReturnObject<CouponActivityDto> getCouponActivityById(@PathVariable Long shopId, @PathVariable Long id);

    /**
     * 店家获取产品信息
     *
     * @param shopId 店铺id
     * @param id     产品id
     */
    @GetMapping("shops/{shopId}/products/{id}")
    InternalReturnObject<ProductDto> getProductByIdAndShopId(@PathVariable Long shopId, @PathVariable Long id);
}

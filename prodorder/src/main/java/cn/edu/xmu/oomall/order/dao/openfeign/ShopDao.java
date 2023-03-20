package cn.edu.xmu.oomall.order.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.FreightDto;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.ShopDto;
import cn.edu.xmu.oomall.order.dao.openfeign.vo.FreightVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "shop-service", qualifier = "shopDao")
public interface ShopDao {
    /**
     * 获得店铺信息
     */
    @GetMapping("/shops/{id}")
    InternalReturnObject<ShopDto> findShop(@PathVariable Long id);

    /**
     * 计算运费，并分包
     *
     * @param id  模板id
     * @param rid 地区id
     */
    @PostMapping("/internal/templates/{id}/regions/{rid}/freightprice")
    InternalReturnObject<FreightDto> getFreightPrice(@PathVariable Long id, @PathVariable Long rid, @RequestBody List<FreightVo> freightVoList);
}

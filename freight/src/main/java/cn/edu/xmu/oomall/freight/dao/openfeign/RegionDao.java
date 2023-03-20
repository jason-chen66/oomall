package cn.edu.xmu.oomall.freight.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.Region;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.SimpleRegion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient("region-service")
public interface RegionDao {
    /**
     * 根据id获取地区
     * @param id 地区id
     * @return 地区
     */
    @GetMapping("/regions/{id}")
    InternalReturnObject<Region> getRegionById(@PathVariable Long id);

    /**
     * 查询地区的上级地区
     * 从最近到最远按序返回最多返回10条
     * @param id
     * @return
     */
    @GetMapping("/internal/regions/{id}/parents")
    InternalReturnObject<List<SimpleRegion>> getParentRegionsById(@PathVariable Long id);
}

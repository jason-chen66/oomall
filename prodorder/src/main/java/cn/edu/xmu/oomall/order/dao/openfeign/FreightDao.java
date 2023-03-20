package cn.edu.xmu.oomall.order.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.order.controller.vo.DeliverVo;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.PackageDto;
import cn.edu.xmu.oomall.order.dao.openfeign.dto.SimplePackageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "freight-service")

public interface FreightDao {
    @GetMapping("/internal/packages/{id}")
    InternalReturnObject<PackageDto>findPackage(@PathVariable Long id);
    @PostMapping("/internal/shops/{shopId}/packages")
    InternalReturnObject<SimplePackageDto>createPackage(@PathVariable Long shopId, @RequestBody DeliverVo deliverVo);
}

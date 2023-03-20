package cn.edu.xmu.oomall.order.dao.openfeign.dto;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.order.dao.openfeign.ShopDao;
import cn.edu.xmu.oomall.order.service.dto.ConsigneeDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopDto {
    @NotNull
    private Long id;

    private String name;

    private Byte type;

    private ConsigneeDto consignee;

    private Integer status;
}

package cn.edu.xmu.oomall.order.dao.openfeign.dto;

import lombok.Data;

import java.util.List;

@Data
public class FreightDto {

    private Integer freightPrice;

    List<PackPriceDto> pack;
}

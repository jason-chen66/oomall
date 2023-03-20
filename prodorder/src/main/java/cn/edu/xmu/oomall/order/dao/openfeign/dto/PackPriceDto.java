package cn.edu.xmu.oomall.order.dao.openfeign.dto;

import lombok.Data;

import java.util.List;

@Data
public class PackPriceDto {

    private List<PackDto> pack;

    private Long price;
}

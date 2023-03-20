package cn.edu.xmu.oomall.freight.service.logistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class GetRouteInfoAdaptorDto {

    /**
     * 操作状态
     * 0. 失败
     * 1. 成功
     */
    Byte status;

    /**
     * 描述
     */
    String msg;

    /**
     * 运单状态,如果是顺丰的话,需要用这个值
     */
    byte expressStatus;
    /**
     * 物流信息
     */
    List<Route> routes;

    @Data
    @AllArgsConstructor
    public static class Route{
        String content;
        LocalDateTime gmtCreate;

    }

}

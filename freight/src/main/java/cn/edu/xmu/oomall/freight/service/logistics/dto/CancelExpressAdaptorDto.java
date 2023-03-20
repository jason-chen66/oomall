package cn.edu.xmu.oomall.freight.service.logistics.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CancelExpressAdaptorDto {

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

}

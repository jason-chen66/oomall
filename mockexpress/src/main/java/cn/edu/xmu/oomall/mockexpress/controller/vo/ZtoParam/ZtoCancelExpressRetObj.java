package cn.edu.xmu.oomall.mockexpress.controller.vo.ZtoParam;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ZtoCancelExpressRetObj {
    /**
     * 返回信息
     */
    String message;
    /**
     * 错误码
     */
    String statusCode;
    /**
     * 是否成功状态
     */
    Boolean status;
}

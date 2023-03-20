package cn.edu.xmu.oomall.freight.service.openfeign.JtParam;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JtCancelExpressRetObj {
    /**
     * 返回信息
     */
    String msg;
    /**
     * 错误码
     */
    String code;

    /**
     * 业务数据
     */
    Result data;

    @Data
    @NoArgsConstructor
    public static class Result{
        String billCode;
        String txlogisticId;
    }

}

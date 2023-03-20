package cn.edu.xmu.oomall.order.dao.openfeign.dto;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Builder
public class PackageDto {

    /**
     * 未发货
     */
    public static final Byte UNSENT = 0;
    /**
     * 在途
     */
    public static final Byte ON = 1;
    /**
     * 签收
     */
    public static final Byte SIGNED = 2;
    /**
     * 取消
     */
    public static final Byte CANCEL = 3;
    /**
     * 拒收
     */
    public static final Byte REFUSED = 4;
    /**
     * 已退回
     */
    public static final Byte RETURNED = 5;
    /**
     *  丢失
     */
    public static final Byte LOST = 6;
    /**
     * 回收
     */
    public static final Byte RECYCLED = 7;
    /**
     * 破损
     */
    public static final Byte BROKEN = 8;

    @NotNull
    private Long id;
    private String billCode;

    private Byte status;


    @Builder
    public PackageDto(Long id,String billCode,Byte status) {
        this.id = id;
        this.billCode = billCode;
        this.status = status;
    }


    /**
     * 判断是否由于快递而需要退款
     *
     */
    public boolean needRefund() throws BusinessException {
        if (status != null) {
            return status.equals(BROKEN) || status.equals(LOST) || status.equals(CANCEL) || status.equals(REFUSED);
        } else {
            throw new BusinessException(ReturnNo.PARAMETER_MISSED);
        }
    }
}

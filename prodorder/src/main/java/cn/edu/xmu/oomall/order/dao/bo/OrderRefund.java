package cn.edu.xmu.oomall.order.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRefund extends OOMallObject implements Serializable {
    @Builder
    public OrderRefund(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified, Long orderId, Long refundId,Long point) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.orderId = orderId;
        this.refundId = refundId;
        this.point = point;
    }

    private Long refundId;
    private Long orderId;
    private Long point;
}

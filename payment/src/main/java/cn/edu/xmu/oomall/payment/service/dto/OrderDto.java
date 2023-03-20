package cn.edu.xmu.oomall.payment.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {
    @Setter
    @Getter
    private Long id;

    @Setter
    @Getter
    private String orderSn;

    @Setter
    @Getter
    private Long shopId;

    @Setter
    @Getter
    private Long pid;

    @Setter
    @Getter
    private String message;

    @Setter
    private Long activityId;

    @Setter
    @Getter
    private Integer status;

    @Setter
    @Getter
    private Long point;
    @Setter
    @Getter
    private Long originPrice;

    @Setter
    @Getter
    private Long discountPrice;

    @Setter
    @Getter
    private List<OrderItemDto> orderItems;
}

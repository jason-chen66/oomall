package cn.edu.xmu.oomall.payment.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDto {
    @Setter
    @Getter
    private Long id;
    @Setter
    @Getter
    private Long orderId;

    @Setter
    @Getter
    private Long onsaleId;


    @Getter
    @Setter
    private Integer quantity;

    @Setter
    @Getter
    private Long price;

    @Setter
    @Getter
    private Long discountPrice;

    @Setter
    @Getter
    private Long point;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private Long actId;


    @Setter
    @Getter
    private Long couponId;


}

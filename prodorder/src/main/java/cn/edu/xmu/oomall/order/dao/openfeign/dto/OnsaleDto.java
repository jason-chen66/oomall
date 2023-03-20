//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao.openfeign.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnsaleDto {

    /**
     * 正常
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte NORMAL = 0;
    /**
     * 秒杀
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte SECONDKILL = 1;
    /**
     * 团购
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte GROUPON = 2;

    /**
     * 预售
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte ADVSALE = 3;

    /**
     * 状态和类名对应
     * @return
     */
    public static final Map<Byte, String> BEANNAMES = new HashMap() {
        {
            put(NORMAL, "NormalProcessor");
            put(SECONDKILL, "NormalProcessor");
            put(GROUPON, "GrouponProcessor");
            put(ADVSALE, "AdvSaleProcessor");
        }
    };

    private Long id;
    private IdNameTypeDto shop;
    private IdNameDto product;
    private Long price;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Integer quantity;
    private Integer maxQuantity;
    private Byte type;
    private List<IdNameTypeDto> actList;
}

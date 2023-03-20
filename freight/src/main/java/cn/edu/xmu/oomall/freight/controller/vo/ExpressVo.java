package cn.edu.xmu.oomall.freight.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ExpressVo {
    @NotNull(message ="商铺渠道不应为空")
    private long shopLogisticId;

    private Consignee sender;

    @NotNull(message = "收货人信息不应为空")
    private Consignee delivery;

    @Data
    public class Consignee{
        /**
         * 姓名
         */
        @NotNull
        private String name;

        @NotNull
        private String mobile;

        @NotNull
        @Min(value = 0,message = "地区号最小值为0")
        private long regionId;

        @NotNull
        private String address;
    }

}

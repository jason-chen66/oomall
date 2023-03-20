package cn.edu.xmu.oomall.freight.service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExpressDto {
        private Long id;

        private String billCode;

        private SimpleLogisticsDto logistics;

        private List<RouteDto> routes;

        private ConsigneeDto shipper;
        private ConsigneeDto receiver;

        private int status;

        private SimpleAdminUserDto creator;
        private SimpleAdminUserDto modifier;

        /**
         * format:datatime
         * description:创建时间
         */
        private String gmtCreate;
        /**
         * format:datatime
         * description:修改时间
         */
        private String gmtModified;
}

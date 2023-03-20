package cn.edu.xmu.oomall.freight.service.openfeign.JtParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中通物流轨迹参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JtRouteResultParam {
    /**
     * 扫描时间
     */
    String scanTime;

    /**
     * 轨迹描述
     */
    String desc;

    /**
     * 扫描类型
     * 1、快件揽收
     *
     * 2、入仓扫描（停用）
     *
     * 3、发件扫描
     *
     * 4、到件扫描
     *
     * 5、出仓扫描
     *
     * 6、入库扫描
     *
     * 7、代理点收入扫描
     *
     * 8、快件取出扫描
     *
     * 9、出库扫描
     *
     * 10、快件签收
     *
     * 11、问题件扫描
     */
    String scanType;

    /**
     * 扫描网点名称
     */
    String scanNetworkName;

    /**
     * 扫描网点ID
     */
    String scanNetworkId;

    /**
     * 业务员姓名
     */
    String staffName;

    /**
     * 业务员联系方式
     */
    String staffContact;

    /**
     * 扫描网点联系方式
     */
    String scanNetworkContact;

    /**
     * 扫描网点省份
     */
    String scanNetworkProvince;

    /**
     * 扫描网点城市
     */
    String scanNetworkCity;

    /**
     * 扫描网点区/县
     */
    String scanNetworkArea;

    /**
     * 上一站(到件)或下一站名称(发件)
     */
    String nextStopName;

    /**
     * 下一站省份（发件扫描类型时提供）
     */
    String nextNetworkProvinceName;

    /**
     * 下一站城市（发件扫描类型时提供）
     */
    String nextNetworkCityName;

    /**
     * 下一站区/县（发件扫描类型时提供）
     */
    String nextNetworkAreaName;
}

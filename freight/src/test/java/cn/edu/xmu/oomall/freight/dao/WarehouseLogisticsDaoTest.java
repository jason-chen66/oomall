package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.FreightApplication;
import cn.edu.xmu.oomall.freight.FreightTestApplication;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseLogistics;
import cn.edu.xmu.oomall.freight.mapper.generator.WarehouseLogisticsPoMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = FreightTestApplication.class)
@Transactional
public class WarehouseLogisticsDaoTest {

    @Autowired
    private WarehouseLogisticsDao warehouseLogisticsDao;

    @MockBean
    private RedisUtil redisUtil;

    @Autowired
    private WarehouseLogisticsPoMapper warehouseLogisticsPoMapper;

    @Test
    public void findByWarehouseIdAndShopLogisticsId(){
        String key = String.format(WarehouseLogisticsDao.WAREHOUSE_LOGISTICS_KEY, 1L,1L);
        Mockito.when(redisUtil.hasKey(key)).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        WarehouseLogistics retWarehouseLogistics = warehouseLogisticsDao.findByWarehouseIdAndShopLogisticsId(1L, 1L);

        assertThat(retWarehouseLogistics.getShopLogisticsId()).isEqualTo(1L);
        assertThat(retWarehouseLogistics.getWarehouseId()).isEqualTo(1L);
        assertThat(retWarehouseLogistics.getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T13:57:43"));
        assertThat(retWarehouseLogistics.getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02T13:57:43"));
        assertThat(retWarehouseLogistics.getInvalid()).isEqualTo(WarehouseLogistics.VALID);

    }

    @Test
    public void insert(){
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);
        WarehouseLogistics warehouseLogistics =WarehouseLogistics.builder()
                .warehouseId(999L)
                .shopLogisticsId(999L)
                .beginTime(LocalDateTime.parse("2022-12-02T13:57:43"))
                .endTime(LocalDateTime.parse("2023-04-02T13:57:43"))
                .invalid(WarehouseLogistics.VALID).build();

        WarehouseLogistics ret = warehouseLogisticsDao.insert(warehouseLogistics, user);

        assertThat(ret.getWarehouseId()).isEqualTo(warehouseLogistics.getWarehouseId());
        assertThat(ret.getShopLogisticsId()).isEqualTo(warehouseLogistics.getShopLogisticsId());
        assertThat(ret.getBeginTime()).isEqualTo(warehouseLogistics.getBeginTime());
        assertThat(ret.getEndTime()).isEqualTo(warehouseLogistics.getEndTime());
        assertThat(ret.getInvalid()).isEqualTo(warehouseLogistics.getInvalid());
    }

    @Test
        public void save(){
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);
        WarehouseLogistics warehouseLogistics =WarehouseLogistics.builder()
                .warehouseId(1L)
                .shopLogisticsId(1L)
                .beginTime(LocalDateTime.parse("2022-12-02T13:57:43"))
                .endTime(LocalDateTime.parse("2023-04-02T13:57:43"))
                .invalid(WarehouseLogistics.VALID).build();
        warehouseLogisticsDao.save(warehouseLogistics, user);
    }


    @Test
    public void retrieveByWarehouseId1(){
        List<WarehouseLogistics> ret = warehouseLogisticsDao.retrieveByWarehouseId(1L,1,10).getList();

        assertThat(ret.get(0).getWarehouseId()).isEqualTo(1L);
        assertThat(ret.get(0).getShopLogisticsId()).isEqualTo(1L);
        assertThat(ret.get(0).getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T13:57:43"));
        assertThat(ret.get(0).getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02T13:57:43"));
        assertThat(ret.get(0).getInvalid()).isEqualTo(WarehouseLogistics.VALID);
        assertThat(ret.get(1).getWarehouseId()).isEqualTo(1L);
        assertThat(ret.get(1).getShopLogisticsId()).isEqualTo(2L);
        assertThat(ret.get(1).getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T13:57:43"));
        assertThat(ret.get(1).getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02T13:57:43"));
        assertThat(ret.get(1).getInvalid()).isEqualTo(WarehouseLogistics.VALID);
        assertThat(ret.get(2).getWarehouseId()).isEqualTo(1L);
        assertThat(ret.get(2).getShopLogisticsId()).isEqualTo(3L);
        assertThat(ret.get(2).getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T13:57:43"));
        assertThat(ret.get(2).getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02T13:57:43"));
        assertThat(ret.get(2).getInvalid()).isEqualTo(WarehouseLogistics.VALID);

    }

    @Test
    public void retrieveByWarehouseIdInternal(){
        String key = String.format(WarehouseLogisticsDao.WAREHOUSE_LOGISTICS_LIST_INTERNAL_KEY,1L);
        Mockito.when(redisUtil.hasKey(key)).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<WarehouseLogistics> ret = warehouseLogisticsDao.retrieveByWarehouseIdInternal(1L);

        assertThat(ret.get(0).getWarehouseId()).isEqualTo(1L);
        assertThat(ret.get(0).getShopLogisticsId()).isEqualTo(1L);
        assertThat(ret.get(0).getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T13:57:43"));
        assertThat(ret.get(0).getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02T13:57:43"));
        assertThat(ret.get(0).getInvalid()).isEqualTo(WarehouseLogistics.VALID);
        assertThat(ret.get(1).getWarehouseId()).isEqualTo(1L);
        assertThat(ret.get(1).getShopLogisticsId()).isEqualTo(2L);
        assertThat(ret.get(1).getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T13:57:43"));
        assertThat(ret.get(1).getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02T13:57:43"));
        assertThat(ret.get(1).getInvalid()).isEqualTo(WarehouseLogistics.VALID);
        assertThat(ret.get(2).getWarehouseId()).isEqualTo(1L);
        assertThat(ret.get(2).getShopLogisticsId()).isEqualTo(3L);
        assertThat(ret.get(2).getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T13:57:43"));
        assertThat(ret.get(2).getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02T13:57:43"));
        assertThat(ret.get(2).getInvalid()).isEqualTo(WarehouseLogistics.VALID);
    }

}

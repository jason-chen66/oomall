package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.FreightApplication;
import cn.edu.xmu.oomall.freight.FreightTestApplication;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.mapper.generator.WarehousePoMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = FreightTestApplication.class)
@Transactional
public class WarehouseDaoTest {

    @Autowired
    private WarehouseDao warehouseDao;

    @MockBean
    private RedisUtil redisUtil;

    @Autowired
    private WarehousePoMapper warehousePoMapper;

    @Test
    public void insert(){
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);
        Warehouse warehouse =Warehouse.builder()
                .name("test")
                .address("testAddress")
                .regionId(1L)
                .senderName("testSender")
                .senderMobile("12345678901")
                .shopId(1L)
                .priority(1000)
                .invalid(Warehouse.VALID)
                .build();

        Warehouse ret = warehouseDao.insert(warehouse, user);

        assertThat(ret.getName()).isEqualTo(warehouse.getName());
        assertThat(ret.getAddress()).isEqualTo(warehouse.getAddress());
        assertThat(ret.getRegionId()).isEqualTo(warehouse.getRegionId());
        assertThat(ret.getSenderName()).isEqualTo(warehouse.getSenderName());
        assertThat(ret.getSenderMobile()).isEqualTo(warehouse.getSenderMobile());
        assertThat(ret.getShopId()).isEqualTo(warehouse.getShopId());
        assertThat(ret.getPriority()).isEqualTo(warehouse.getPriority());
        assertThat(ret.getInvalid()).isEqualTo(warehouse.getInvalid());
    }


    @Test
    public void retrieveByShopId1(){
        List<Warehouse> ret = warehouseDao.retrieveByShopId(5L,1,10).getList();

        assertThat(ret.get(0).getId()).isEqualTo(5L);
        assertThat(ret.get(0).getName()).isEqualTo("芳城园一区仓库");
        assertThat(ret.get(0).getAddress()).isEqualTo("北京,丰台,方庄,芳城园一区曙光路14号");
        assertThat(ret.get(0).getRegionId()).isEqualTo(1396L);
        assertThat(ret.get(0).getSenderName()).isEqualTo("张三");
        assertThat(ret.get(0).getSenderMobile()).isEqualTo("139144166588");
        assertThat(ret.get(0).getShopId()).isEqualTo(5L);
        assertThat(ret.get(0).getPriority()).isEqualTo(1000);
        assertThat(ret.get(0).getInvalid()).isEqualTo(Warehouse.VALID);

    }

    @Test
    public void findById(){
        String key = String.format(WarehouseDao.WAREHOUSE_KEY, 1L);
        Mockito.when(redisUtil.hasKey(key)).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Warehouse ret = warehouseDao.findById(1L);

        assertThat(ret.getId()).isEqualTo(1L);
        assertThat(ret.getName()).isEqualTo("朝阳新城第二仓库");
        assertThat(ret.getAddress()).isEqualTo("北京,朝阳,东坝,朝阳新城第二曙光路14号");
        assertThat(ret.getRegionId()).isEqualTo(1043L);
        assertThat(ret.getSenderName()).isEqualTo("阮杰");
        assertThat(ret.getSenderMobile()).isEqualTo("139542562579");
        assertThat(ret.getShopId()).isEqualTo(1L);
        assertThat(ret.getPriority()).isEqualTo(1000);
        assertThat(ret.getInvalid()).isEqualTo(Warehouse.VALID);
    }

    @Test
    public void save(){
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);
        Warehouse warehouse =Warehouse.builder()
                .name("test")
                .address("testAddress")
                .regionId(1L)
                .senderName("testSender")
                .senderMobile("12345678901")
                .shopId(1L)
                .priority(1000)
                .invalid(Warehouse.VALID)
                .build();
        warehouseDao.save(warehouse,user);
    }

    @Test
    public void retrieveByShopIdInternal(){
        String key = String.format(WarehouseDao.SHOP_WAREHOUSE_LIST_INTERNAL_KEY, 5L);
        Mockito.when(redisUtil.hasKey(key)).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Warehouse> ret = warehouseDao.retrieveByShopIdInternal(5L);

        assertThat(ret.get(0).getId()).isEqualTo(5L);
        assertThat(ret.get(0).getName()).isEqualTo("芳城园一区仓库");
        assertThat(ret.get(0).getAddress()).isEqualTo("北京,丰台,方庄,芳城园一区曙光路14号");
        assertThat(ret.get(0).getRegionId()).isEqualTo(1396L);
        assertThat(ret.get(0).getSenderName()).isEqualTo("张三");
        assertThat(ret.get(0).getSenderMobile()).isEqualTo("139144166588");
        assertThat(ret.get(0).getShopId()).isEqualTo(5L);
        assertThat(ret.get(0).getPriority()).isEqualTo(1000);
        assertThat(ret.get(0).getInvalid()).isEqualTo(Warehouse.VALID);
    }

}

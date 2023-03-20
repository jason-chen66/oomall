package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.FreightApplication;
import cn.edu.xmu.oomall.freight.FreightTestApplication;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.SimpleRegion;
import cn.edu.xmu.oomall.freight.mapper.generator.WarehouseRegionPoMapper;
import com.github.pagehelper.PageInfo;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = FreightTestApplication.class)
@Transactional
public class WarehouseRegionDaoTest {

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private RegionDao regionDao;

    @MockBean
    private WarehouseDao warehouseDao;

    @Autowired
    private WarehouseRegionDao warehouseRegionDao;

    @Autowired
    private WarehouseRegionPoMapper warehouseRegionPoMapper;

    @Test
    public void retrieveAllByRegionId1() {
        List<SimpleRegion> simpleRegionList= new ArrayList<>();
        simpleRegionList.add(SimpleRegion.builder().id(7362L).build());
        InternalReturnObject<List<SimpleRegion>> internalReturnObject = new InternalReturnObject<>(simpleRegionList);
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.OK.getMessage());
        Mockito.when(regionDao.getParentRegionsById(1L)).thenReturn(internalReturnObject);
        List<Warehouse> warehouseRegionList = new ArrayList<>();
        warehouseRegionList.add(Warehouse.builder().id(6L).build());
        warehouseRegionList.add(Warehouse.builder().id(7L).build());
        Mockito.when(warehouseDao.retrieveByShopIdInternal(1L)).thenReturn(warehouseRegionList);

        List<WarehouseRegion> ret = warehouseRegionDao.retrieveAllByRegionId(1L, 1L,1,10).getList();

        assertThat(ret.get(0).getRegionId()).isEqualTo(1L);
        assertThat(ret.get(0).getWarehouseId()).isEqualTo(6L);
        assertThat(ret.get(0).getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T14:20:05"));
        assertThat(ret.get(0).getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02T14:20:05"));
        assertThat(ret.get(1).getRegionId()).isEqualTo(7362L);
        assertThat(ret.get(1).getWarehouseId()).isEqualTo(7L);
        assertThat(ret.get(1).getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T14:20:05"));
        assertThat(ret.get(1).getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02T14:20:05"));
    }

    @Test
    public void insert(){
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);
        WarehouseRegion warehouseRegion = WarehouseRegion.builder()
                .regionId(1L)
                .warehouseId(1L)
                .beginTime(LocalDateTime.parse("2022-12-01T00:00:00"))
                .endTime(LocalDateTime.parse("2022-12-31T23:59:59")
                ).build();

        WarehouseRegion retWarehouseRegion = warehouseRegionDao.insert(warehouseRegion, user,1L);

        assertThat(retWarehouseRegion.getRegionId()).isEqualTo(warehouseRegion.getRegionId());
        assertThat(retWarehouseRegion.getWarehouseId()).isEqualTo(warehouseRegion.getWarehouseId());
        assertThat(retWarehouseRegion.getBeginTime()).isEqualTo(warehouseRegion.getBeginTime());
        assertThat(retWarehouseRegion.getEndTime()).isEqualTo(warehouseRegion.getEndTime());
    }

    @Test
    public void findByWarehouseIdAndRegionId(){
        String key = String.format(WarehouseRegionDao.WAREHOUSE_REGION_KEY, 1L, 1L);
        Mockito.when(redisUtil.hasKey(key)).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        WarehouseRegion retWarehouseRegion = warehouseRegionDao.findByWarehouseIdAndRegionId(1L, 1L);
        assertThat(retWarehouseRegion.getRegionId()).isEqualTo(1L);
        assertThat(retWarehouseRegion.getWarehouseId()).isEqualTo(1L);
        assertThat(retWarehouseRegion.getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T14:20:05"));
        assertThat(retWarehouseRegion.getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02T14:20:05"));
    }

    @Test
    public void save(){
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);
        WarehouseRegion warehouseRegion = WarehouseRegion.builder()
                .regionId(1L)
                .warehouseId(1L)
                .beginTime(LocalDateTime.parse("2022-12-01T00:00:00"))
                .endTime(LocalDateTime.parse("2022-12-31T23:59:59")
                ).build();
       warehouseRegionDao.save(warehouseRegion, user,1L);
    }

    @Test
    public void retrieveByWarehouseId1(){
        List<WarehouseRegion> ret = warehouseRegionDao.retrieveByWarehouseId(1L,1,10).getList();

        assertThat(ret.get(0).getRegionId()).isEqualTo(1L);
        assertThat(ret.get(0).getWarehouseId()).isEqualTo(1L);
        assertThat(ret.get(0).getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T14:20:05"));
        assertThat(ret.get(0).getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02T14:20:05"));
    }

    @Test
    public void retrieveByWarehouseIdInternal(){
        String key = String.format(WarehouseRegionDao.WAREHOUSE_REGION_LIST_INTERNAL_KEY, 1L);
        Mockito.when(redisUtil.hasKey(key)).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<WarehouseRegion> ret = warehouseRegionDao.retrieveByWarehouseIdInternal(1L);

        assertThat(ret.get(0).getRegionId()).isEqualTo(1L);
        assertThat(ret.get(0).getWarehouseId()).isEqualTo(1L);
        assertThat(ret.get(0).getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T14:20:05"));
        assertThat(ret.get(0).getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02T14:20:05"));
    }

    //
    @Test
    public void delete(){
        WarehouseRegion warehouseRegion = WarehouseRegion.builder()
                .regionId(1L)
                .warehouseId(1L)
                .build();
        warehouseRegionDao.delete(warehouseRegion, 1L);
    }

    @Test
    public void retrieveAllByRegionIdInternal(){
        String key = String.format(WarehouseRegionDao.REGION_WAREHOUSE_LIST_INTERNAL_KEY, 1L,1L);
        Mockito.when(redisUtil.hasKey(key)).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        List<SimpleRegion> simpleRegionList= new ArrayList<>();
        simpleRegionList.add(SimpleRegion.builder().id(7362L).build());
        InternalReturnObject<List<SimpleRegion>> internalReturnObject = new InternalReturnObject<>(simpleRegionList);
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.OK.getMessage());
        Mockito.when(regionDao.getParentRegionsById(1L)).thenReturn(internalReturnObject);
        List<Warehouse> warehouseRegionList = new ArrayList<>();
        warehouseRegionList.add(Warehouse.builder().id(6L).build());
        warehouseRegionList.add(Warehouse.builder().id(7L).build());
        Mockito.when(warehouseDao.retrieveByShopIdInternal(1L)).thenReturn(warehouseRegionList);

        List<WarehouseRegion> ret = warehouseRegionDao.retrieveAllByRegionIdInternal(1L, 1L);

        assertThat(ret.get(0).getRegionId()).isEqualTo(1L);
        assertThat(ret.get(0).getWarehouseId()).isEqualTo(6L);
        assertThat(ret.get(0).getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T14:20:05"));
        assertThat(ret.get(0).getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02T14:20:05"));
        assertThat(ret.get(1).getRegionId()).isEqualTo(7362L);
        assertThat(ret.get(1).getWarehouseId()).isEqualTo(7L);
        assertThat(ret.get(1).getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T14:20:05"));
        assertThat(ret.get(1).getEndTime()).isEqualTo(LocalDateTime.parse("2023-04-02T14:20:05"));
    }
}

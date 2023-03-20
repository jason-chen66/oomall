package cn.edu.xmu.oomall.freight.Controller;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.FreightTestApplication;
import cn.edu.xmu.oomall.freight.controller.vo.*;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.Region;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseLogistics;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.SimpleRegion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = FreightTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class AdminFreightControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private RegionDao regionDao;

    private static String adminToken;

    @BeforeAll
    public static void setup(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    @Test
    public void retrieveRegionWarehouses() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        InternalReturnObject<List<SimpleRegion>> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.OK.getMessage());
        List<SimpleRegion> simpleRegionList= new ArrayList<>();
        simpleRegionList.add(SimpleRegion.builder().id(420824L).build());
        internalReturnObject.setData(simpleRegionList);
        Mockito.when(regionDao.getParentRegionsById(1L)).thenReturn(internalReturnObject);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/1/regions/1/warehouses").header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].warehouse.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].warehouse.name", is("朝阳新城第二仓库")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].warehouse.invalid", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].warehouse.priority", is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].beginTime", is("2022-12-02T14:20:05")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].endTime", is("2023-04-02T14:20:05")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].warehouse.id", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].warehouse.name", is("苏湖林场仓库")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].warehouse.invalid", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].warehouse.priority", is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].beginTime", is("2022-12-02T14:20:05")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].endTime", is("2023-04-02T14:20:05")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].warehouse.id", is(21)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].warehouse.name", is("石龙仓库")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].warehouse.invalid", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].warehouse.priority", is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].beginTime", is("2022-12-02T14:20:05")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].endTime", is("2023-04-02T14:20:05")))
                .andDo(MockMvcResultHandlers.print());

    }

    /**
     * 管理员查询该地区所有配送仓库
     */
    @Test
    public void retrieveRegionWarehouses1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        InternalReturnObject<List<SimpleRegion>> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.OK.getMessage());
        List<SimpleRegion> simpleRegionList= new ArrayList<>();
        simpleRegionList.add(SimpleRegion.builder().id(7362L).build());
        internalReturnObject.setData(simpleRegionList);
        Mockito.when(regionDao.getParentRegionsById(13267L)).thenReturn(internalReturnObject);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/0/regions/13267/warehouses").header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].warehouse.id", is(7)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].warehouse.name", is("长张屯仓库")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].warehouse.invalid", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].warehouse.priority", is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].beginTime", is("2022-12-02T14:20:05")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].endTime", is("2023-04-02T14:20:05")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].warehouse.id", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].warehouse.name", is("道北村仓库")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].warehouse.invalid", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].warehouse.priority", is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].beginTime", is("2022-12-02T14:20:05")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].endTime", is("2023-04-02T14:20:05")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].warehouse.id", is(22)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].warehouse.name", is("小庄村仓库")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].warehouse.invalid", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].warehouse.priority", is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].beginTime", is("2022-12-02T14:20:05")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].endTime", is("2023-04-02T14:20:05")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[3].warehouse.id", is(23)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[3].warehouse.name", is("东王庄仓库")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[3].warehouse.invalid", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[3].warehouse.priority", is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[3].beginTime", is("2022-12-02T14:20:05")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[3].endTime", is("2023-04-02T14:20:05")))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test //创建成功
    public void createWarehouseRegions1() throws Exception {
        WarehouseRegionVo vo = new WarehouseRegionVo();
        vo.setBeginTime(LocalDateTime.parse("2022-11-02T14:20:05"));
        vo.setEndTime(LocalDateTime.parse("2023-03-02T14:20:05"));
        InternalReturnObject<Region> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.OK.getMessage());
        internalReturnObject.setData(new Region());
        Mockito.when(regionDao.getRegionById(7362L)).thenReturn(internalReturnObject);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/1/warehouses/1/regions/7362")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("创建成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test //仓库不存在
    public void createWarehouseRegions2() throws Exception {
        WarehouseRegionVo vo = new WarehouseRegionVo();
        vo.setBeginTime(LocalDateTime.parse("2022-11-02T14:20:05"));
        vo.setEndTime(LocalDateTime.parse("2023-03-02T14:20:05"));
        InternalReturnObject<Region> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.OK.getMessage());
        internalReturnObject.setData(new Region());
        Mockito.when(regionDao.getRegionById(7362L)).thenReturn(internalReturnObject);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/1/warehouses/100/regions/7362")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test //开始时间晚于结束时间
    public void createWarehouseRegions3() throws Exception{
        WarehouseRegionVo vo = new WarehouseRegionVo();
        vo.setBeginTime(LocalDateTime.parse("2023-11-02T14:20:05"));
        vo.setEndTime(LocalDateTime.parse("2023-03-02T14:20:05"));
        InternalReturnObject<Region> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.OK.getMessage());
        internalReturnObject.setData(new Region());
        Mockito.when(regionDao.getRegionById(7362L)).thenReturn(internalReturnObject);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/1/warehouses/1/regions/7362")
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.LATE_BEGINTIME.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test //地区不存在
    public void createWarehouseRegions4() throws Exception{
        WarehouseRegionVo vo = new WarehouseRegionVo();
        vo.setBeginTime(LocalDateTime.parse("2022-11-02T14:20:05"));
        vo.setEndTime(LocalDateTime.parse("2023-03-02T14:20:05"));
        InternalReturnObject<Region> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage());
        internalReturnObject.setData(new Region());
        Mockito.when(regionDao.getRegionById(99999L)).thenReturn(internalReturnObject);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/1/warehouses/1/regions/99999")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test //仓库地区已存在
    public void createWarehouseRegions5() throws Exception{
        WarehouseRegionVo vo = new WarehouseRegionVo();
        vo.setBeginTime(LocalDateTime.parse("2022-11-02T14:20:05"));
        vo.setEndTime(LocalDateTime.parse("2023-03-02T14:20:05"));
        InternalReturnObject<Region> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.OK.getMessage());
        internalReturnObject.setData(new Region());
        Mockito.when(regionDao.getRegionById(1L)).thenReturn(internalReturnObject);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/1/warehouses/1/regions/1")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.FREIGHT_WAREHOUSEREGION_EXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateWarehouseRegions() throws Exception{
        WarehouseRegionVo vo = new WarehouseRegionVo();
        vo.setBeginTime(LocalDateTime.parse("2022-11-02T14:20:05"));
        vo.setEndTime(LocalDateTime.parse("2023-03-02T14:20:05"));
        InternalReturnObject<Region> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.OK.getMessage());
        internalReturnObject.setData(new Region());
        Mockito.when(regionDao.getRegionById(1L)).thenReturn(internalReturnObject);

            this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/1/warehouses/1/regions/1")
                            .header("authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                    .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteWarehouseRegion() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/1/warehouses/1/regions/1")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void retrieveWarehousesRegions() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        InternalReturnObject<Region> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.OK.getMessage());
        Region region = new Region();
        region.setId(1L);
        region.setName("北京市");
        internalReturnObject.setData(region);
        Mockito.when(regionDao.getRegionById(1L)).thenReturn(internalReturnObject);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/1/warehouses/1/regions").header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].region.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].region.name", is("北京市")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].beginTime", is("2022-12-02T14:20:05")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].endTime", is("2023-04-02T14:20:05")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void createWarehouse() throws Exception{
        WarehouseVo vo = new WarehouseVo();
        vo.setName("测试仓库");
        vo.setAddress("测试地址");
        vo.setRegionId(1L);
        vo.setSenderName("测试发件人");
        vo.setSenderMobile("12345678901");
        InternalReturnObject<Region> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.OK.getMessage());
        Region region = new Region();
        region.setId(1L);
        region.setName("北京市");
        internalReturnObject.setData(region);
        Mockito.when(regionDao.getRegionById(1L)).thenReturn(internalReturnObject);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/1/warehouses")
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("创建成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void retrieveWarehouses() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        InternalReturnObject<Region> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.OK.getMessage());
        Region region = new Region();
        region.setId(797L);
        internalReturnObject.setData(region);
        Mockito.when(regionDao.getRegionById(797L)).thenReturn(internalReturnObject);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/4/warehouses").header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].name", is("牌坊仓库")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].address", is("北京,朝阳,小红门,牌坊曙光路14号")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].region.id", is(797)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].senderName", is("张三")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].senderMobile", is("139266427223")))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 管理员查询所有仓库
     * @throws Exception
     */
    @Test
    public void retrieveWarehouses1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        InternalReturnObject<Region> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.OK.getMessage());
        Region region = new Region();
        region.setId(1043L);
        internalReturnObject.setData(region);
        Mockito.when(regionDao.getRegionById(Mockito.anyLong())).thenReturn(internalReturnObject);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/0/warehouses").header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].name", is("朝阳新城第二仓库")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].address", is("北京,朝阳,东坝,朝阳新城第二曙光路14号")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].region.id", is(1043)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].senderName", is("阮杰")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].senderMobile", is("139542562579")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateWarehouse() throws Exception {
        WarehouseVo vo = new WarehouseVo();
        vo.setName("测试仓库");
        vo.setAddress("测试地址");
        vo.setRegionId(1L);
        vo.setSenderName("测试发件人");
        vo.setSenderMobile("12345678901");
        InternalReturnObject<Region> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.OK.getMessage());
        Region region = new Region();
        region.setId(1L);
        region.setName("北京市");
        internalReturnObject.setData(region);
        Mockito.when(regionDao.getRegionById(1L)).thenReturn(internalReturnObject);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/1/warehouses/1")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteWarehouse() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/1/warehouses/1")
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateWarehouseInvalid() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/1/warehouse/1/suspend")
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateWarehouseValid() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/1/warehouse/1/resume")
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void createWarehouseLogistics() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        WarehosueLogisticsVo vo = new WarehosueLogisticsVo();
        vo.setBeginTime(LocalDateTime.parse("2022-11-02T14:20:05"));
        vo.setEndTime(LocalDateTime.parse("2023-03-02T14:20:05"));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/1/warehouses/2/shoplogistics/1")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("创建成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test //开始时间晚于结束时间
    public void createWarehouseLogistics1() throws Exception{
        WarehouseLogistics vo = new WarehouseLogistics();
        vo.setBeginTime(LocalDateTime.parse("2024-11-02T14:20:05"));
        vo.setEndTime(LocalDateTime.parse("2023-03-02T14:20:05"));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/1/warehouses/2/shoplogistics/1")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.LATE_BEGINTIME.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test //已有该仓库物流
    public void createWarehouseLogistics2() throws Exception{
        WarehouseLogistics vo = new WarehouseLogistics();
        vo.setBeginTime(LocalDateTime.parse("2022-11-02T14:20:05"));
        vo.setEndTime(LocalDateTime.parse("2023-03-02T14:20:05"));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/1/warehouses/1/shoplogistics/2")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.FREIGHT_WAREHOUSELOGISTIC_EXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateWarehouseLogistics() throws Exception{
        WarehosueLogisticsVo vo = new WarehosueLogisticsVo();
        vo.setBeginTime(LocalDateTime.parse("2022-11-02T14:20:05"));
        vo.setEndTime(LocalDateTime.parse("2023-03-02T14:20:05"));
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/1/warehouses/1/shoplogistics/1")
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteWarehouseLogistics() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/1/warehouses/1/shoplogistics/1")
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void retrieveWarehousesLogistics() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/1/warehouses/1/shoplogistics").header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].shopLogistics.id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].shopLogistics.logistics.id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].shopLogistics.logistics.name", is("极兔速递")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].shopLogistics.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].shopLogistics.logistics.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].shopLogistics.logistics.name", is("顺丰快递")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].shopLogistics.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].shopLogistics.logistics.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].shopLogistics.logistics.name", is("中通快递")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void createShopLogistics1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        ShopLogisticsVo vo = new ShopLogisticsVo();
        vo.setLogisticsId(2L);
        vo.setSecret("test");
        vo.setPriority(11);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/2/shoplogistics")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("创建成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    //平台物流不存在
    @Test
    public void createShopLogistics2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        ShopLogisticsVo vo = new ShopLogisticsVo();
        vo.setLogisticsId(7L);
        vo.setSecret("test");
        vo.setPriority(1111);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/2/shoplogistics")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

    //店铺物流已存在
    @Test
    public void createShopLogistics3() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        ShopLogisticsVo vo = new ShopLogisticsVo();
        vo.setLogisticsId(1L);
        vo.setSecret("test");
        vo.setPriority(11);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/2/shoplogistics")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.FREIGHT_LOGISTIC_EXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void retrieveShopLogistics() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/1/shoplogistics").header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].logistics.id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].logistics.name", is("极兔速递")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].logistics.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].logistics.name", is("顺丰快递")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].logistics.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].logistics.name", is("中通快递")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateShopLogistics() throws Exception{
        ShopLogisticsVo vo = new ShopLogisticsVo();
        vo.setSecret("test");
        vo.setPriority(11);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/1/shoplogistics/1")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateShopLogisticsInValid() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/1/shoplogistics/1/suspend")
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateShopLogisticsValid() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/1/shoplogistics/1/resume")
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findLogisticsNameByBillCode1() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/1/logistics")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("billCode","ZTO123456789123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].name", is("中通快递")))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void findLogisticsNameByBillCode2() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/1/logistics")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].name", is("顺丰快递")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].name", is("中通快递")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].name", is("极兔速递")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findLogisticsNameByBillCode3() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/1/logistics")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("billCode","test"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void createShopUndeliverableRegion1() throws Exception{
        ShopUndeliverableRegionVo vo = new ShopUndeliverableRegionVo();
        vo.setBeginTime(LocalDateTime.parse("2022-11-02T14:20:05"));
        vo.setEndTime(LocalDateTime.parse("2023-03-02T14:20:05"));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/1/shoplogistics/1/regions/1/undeliverable")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void createShopUndeliverableRegion2() throws Exception{
        ShopUndeliverableRegionVo vo = new ShopUndeliverableRegionVo();
        vo.setBeginTime(LocalDateTime.parse("2022-11-02T14:20:05"));
        vo.setEndTime(LocalDateTime.parse("2023-03-02T14:20:05"));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/1/shoplogistics/1/regions/483250/undeliverable")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.FREIGHT_LOGISTIC_EXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateShopUndeliverableRegion1() throws Exception{
        ShopUndeliverableRegionVo vo = new ShopUndeliverableRegionVo();
        vo.setBeginTime(LocalDateTime.parse("2022-11-02T14:20:05"));
        vo.setEndTime(LocalDateTime.parse("2023-03-02T14:20:05"));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/1/shoplogistics/1/regions/483250/undeliverable")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateShopUndeliverableRegion2() throws Exception{
        ShopUndeliverableRegionVo vo = new ShopUndeliverableRegionVo();
        vo.setBeginTime(LocalDateTime.parse("2022-11-02T14:20:05"));
        vo.setEndTime(LocalDateTime.parse("2023-03-02T14:20:05"));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/1/shoplogistics/1/regions/48/undeliverable")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteShopUndeliverableRegion1() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/1/shoplogistics/1/regions/483250/undeliverable")
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("成功")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteShopUndeliverableRegion2() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/1/shoplogistics/1/regions/483/undeliverable")
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void retrieveShopUndeliverableRegions1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        InternalReturnObject<Region> o5 = new InternalReturnObject<>();
        o5.setErrno(ReturnNo.OK.getErrNo());
        o5.setErrmsg(ReturnNo.OK.getMessage());
        Region r5 = new Region();
        r5.setId(483250L);
        r5.setName("厦门市");
        o5.setData(r5);
        Mockito.when(regionDao.getRegionById(483250L)).thenReturn(o5);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/1/shoplogistics/1/undeliverableregions")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page","1")
                        .param("pageSize","10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].region.id", is(483250)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void retrieveShopUndeliverableRegions2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/1/shoplogistics/3/undeliverableregions")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page","1")
                        .param("pageSize","10"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

}

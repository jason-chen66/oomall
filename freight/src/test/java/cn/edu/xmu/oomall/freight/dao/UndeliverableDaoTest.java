package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.FreightTestApplication;
import cn.edu.xmu.oomall.freight.dao.bo.Undeliverable;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = FreightTestApplication.class)
@Transactional
public class UndeliverableDaoTest {

    @Autowired
    private UndeliverableDao undeliverableDao;

    @MockBean
    private RedisUtil redisUtil;

    @Test
    public void findByShopLogisticsIdAndRegionId1(){
        Undeliverable ret=undeliverableDao.findByShopLogisticsIdAndRegionId(1L,483250L);

        assertThat(ret.getId()).isEqualTo(1L);
        assertThat(ret.getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T22:28:43"));
        assertThat(ret.getEndTime()).isEqualTo(LocalDateTime.parse("2023-12-02T22:28:49"));
    }


    @Test
    public void insertTest(){
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);
        Undeliverable undeliverable=Undeliverable.builder()
                .regionId(1L)
                .shopLogisticsId(1L)
                .beginTime(LocalDateTime.parse("2022-12-02T22:28:43"))
                .endTime(LocalDateTime.parse("2022-12-02T22:28:58"))
                .build();

        Undeliverable ret=undeliverableDao.insert(undeliverable,user);

        assertThat(ret.getRegionId()).isEqualTo(undeliverable.getRegionId());
        assertThat(ret.getShopLogisticsId()).isEqualTo(undeliverable.getShopLogisticsId());
        assertThat(ret.getBeginTime()).isEqualTo(undeliverable.getBeginTime());
        assertThat(ret.getEndTime()).isEqualTo(undeliverable.getEndTime());
    }

    @Test
    public void saveTest(){
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);

        Undeliverable undeliverable=Undeliverable.builder()
                .regionId(483250L)
                .shopLogisticsId(1L)
                .beginTime(LocalDateTime.parse("2022-12-02T22:28:43"))
                .endTime(LocalDateTime.parse("2022-12-02T22:28:58"))
                .build();

        ReturnObject ret=undeliverableDao.save(undeliverable,user);

        assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }

    @Test
    public void retrieveByShopLogisticsId1(){
        List<Undeliverable> undeliverableList =new ArrayList<>();
        undeliverableList.add(Undeliverable.builder()
                .regionId(1L)
                .beginTime(LocalDateTime.parse("2022-12-24T16:12:12"))
                .endTime(LocalDateTime.parse("2022-12-24T16:12:55"))
                .build());
        undeliverableList.add(Undeliverable.builder()
                .regionId(4L)
                .beginTime(LocalDateTime.parse("2022-12-23T16:12:12"))
                .endTime(LocalDateTime.parse("2022-12-24T16:12:12"))
                .build());

        String key=String.format(UndeliverableDao.SHOP_LOGISTICS_KEY,1L);
        Mockito.when(redisUtil.hasKey(key)).thenReturn(true);
        Mockito.when(redisUtil.get(key)).thenReturn((Serializable) undeliverableList);

        List<Undeliverable> ret=undeliverableDao.retrieveByShopLogisticsId(1L,1,10).getList();

        assertThat(ret.get(0).getRegionId()).isEqualTo(undeliverableList.get(0).getRegionId());
        assertThat(ret.get(0).getBeginTime()).isEqualTo(undeliverableList.get(0).getBeginTime());
        assertThat(ret.get(0).getEndTime()).isEqualTo(undeliverableList.get(0).getEndTime());
        assertThat(ret.get(1).getRegionId()).isEqualTo(undeliverableList.get(1).getRegionId());
        assertThat(ret.get(1).getBeginTime()).isEqualTo(undeliverableList.get(1).getBeginTime());
        assertThat(ret.get(1).getEndTime()).isEqualTo(undeliverableList.get(1).getEndTime());
    }

    @Test
    public void retrieveByShopLogisticsId2(){
        String key=String.format(UndeliverableDao.SHOP_LOGISTICS_KEY,1L);
        Mockito.when(redisUtil.hasKey(key)).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Undeliverable> ret=undeliverableDao.retrieveByShopLogisticsId(1L,1,10).getList();

        assertThat(ret.get(0).getId()).isEqualTo(1L);
        assertThat(ret.get(0).getRegionId()).isEqualTo(483250L);
        assertThat(ret.get(0).getBeginTime()).isEqualTo(LocalDateTime.parse("2022-12-02T22:28:43"));
        assertThat(ret.get(0).getEndTime()).isEqualTo(LocalDateTime.parse("2023-12-02T22:28:49"));
    }

    @Test
    public void deleteByShopLogisticsIdAndRegionId(){
        ReturnObject ret=undeliverableDao.deleteByShopLogisticsIdAndRegionId(1L,483250L);
        assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }


//    @Test
//    public void retrieveByRegionId1(){
//
//        Set<ShopLogistics> ret=undeliverableDao.retrieveByRegionId(483250L);
//
//    }



}

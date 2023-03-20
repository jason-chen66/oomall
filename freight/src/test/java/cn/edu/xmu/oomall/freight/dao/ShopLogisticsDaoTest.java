package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.FreightApplication;
import cn.edu.xmu.oomall.freight.FreightTestApplication;
import cn.edu.xmu.oomall.freight.dao.bo.ShopLogistics;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = FreightTestApplication.class)
@Transactional
public class ShopLogisticsDaoTest {

    @Autowired
    private ShopLogisticsDao shopLogisticsDao;

    @MockBean
    private RedisUtil redisUtil;


    @Test
    public void findById1(){
        ShopLogistics shopLogistics=ShopLogistics.builder().shopId(1L).logisticsId(1L)
                .invalid(ShopLogistics.VALID).secret("secret1").priority(3).build();

        String key=String.format(ShopLogisticsDao.KEY,1L);
        Mockito.when(redisUtil.hasKey(key)).thenReturn(true);
        Mockito.when(redisUtil.get(key)).thenReturn((ShopLogistics) shopLogistics);

        ShopLogistics ret=shopLogisticsDao.findById(1L);
        assertThat(ret.getShopId()).isEqualTo(shopLogistics.getShopId());
        assertThat(ret.getLogisticsId()).isEqualTo(shopLogistics.getLogisticsId());
        assertThat(ret.getInvalid()).isEqualTo(shopLogistics.getInvalid());
        assertThat(ret.getSecret()).isEqualTo(shopLogistics.getSecret());
        assertThat(ret.getPriority()).isEqualTo(shopLogistics.getPriority());
    }

    @Test
    public void findById2(){
        String key=String.format(ShopLogisticsDao.KEY,1L);
        Mockito.when(redisUtil.hasKey(key)).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        ShopLogistics ret=shopLogisticsDao.findById(1L);

        assertThat(ret.getShopId()).isEqualTo(1L);
        assertThat(ret.getLogisticsId()).isEqualTo(1L);
        assertThat(ret.getSecret()).isEqualTo("secret1");
        assertThat(ret.getPriority()).isEqualTo(3L);
        assertThat(ret.getInvalid()).isEqualTo(ShopLogistics.VALID);
    }

    @Test
    public void findByShopIdAndLogisticsId1(){
        ShopLogistics ret=shopLogisticsDao.findByShopIdAndLogisticsId(1L,1L);

        assertThat(ret.getId()).isEqualTo(1L);
        assertThat(ret.getSecret()).isEqualTo("secret1");
        assertThat(ret.getPriority()).isEqualTo(3L);
        assertThat(ret.getInvalid()).isEqualTo(ShopLogistics.VALID);
    }

    @Test
    public void retrieveByShopId1(){
        List<ShopLogistics> shopLogisticsList=new ArrayList<>();
        shopLogisticsList.add(ShopLogistics.builder().id(1L)
                .logisticsId(1L)
                .invalid(ShopLogistics.VALID)
                .priority(1)
                .secret("secret1").build());
        shopLogisticsList.add(ShopLogistics.builder().id(2L)
                .logisticsId(2L)
                .invalid(ShopLogistics.VALID)
                .priority(2)
                .secret("secret2").build());

        String key=String.format(ShopLogisticsDao.LIST_KEY,1L);
        Mockito.when(redisUtil.hasKey(key)).thenReturn(true);
        Mockito.when(redisUtil.get(key)).thenReturn((Serializable) shopLogisticsList);

        List<ShopLogistics> ret=shopLogisticsDao.retrieveByShopId(1L,1,10).getList();

        assertThat(ret.get(0).getId()).isEqualTo(shopLogisticsList.get(0).getId());
        assertThat(ret.get(0).getLogisticsId()).isEqualTo(shopLogisticsList.get(0).getLogisticsId());
        assertThat(ret.get(0).getInvalid()).isEqualTo(shopLogisticsList.get(0).getInvalid());
        assertThat(ret.get(0).getPriority()).isEqualTo(shopLogisticsList.get(0).getPriority());
        assertThat(ret.get(0).getSecret()).isEqualTo(shopLogisticsList.get(0).getSecret());
        assertThat(ret.get(1).getId()).isEqualTo(shopLogisticsList.get(1).getId());
        assertThat(ret.get(1).getLogisticsId()).isEqualTo(shopLogisticsList.get(1).getLogisticsId());
        assertThat(ret.get(1).getInvalid()).isEqualTo(shopLogisticsList.get(1).getInvalid());
        assertThat(ret.get(1).getPriority()).isEqualTo(shopLogisticsList.get(1).getPriority());
        assertThat(ret.get(1).getSecret()).isEqualTo(shopLogisticsList.get(1).getSecret());

    }

    @Test
    public void retrieveByShopId2(){
        String key=String.format(ShopLogisticsDao.KEY,1L);
        Mockito.when(redisUtil.hasKey(key)).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<ShopLogistics> ret=shopLogisticsDao.retrieveByShopId(1L,1,10).getList();

        assertThat(ret.get(0).getId()).isEqualTo(1L);
        assertThat(ret.get(0).getLogisticsId()).isEqualTo(1L);
        assertThat(ret.get(0).getInvalid()).isEqualTo(ShopLogistics.VALID);
        assertThat(ret.get(0).getPriority()).isEqualTo(3);
        assertThat(ret.get(0).getSecret()).isEqualTo("secret1");
        assertThat(ret.get(1).getId()).isEqualTo(2L);
        assertThat(ret.get(1).getLogisticsId()).isEqualTo(2L);
        assertThat(ret.get(1).getInvalid()).isEqualTo(ShopLogistics.VALID);
        assertThat(ret.get(1).getPriority()).isEqualTo(113);
        assertThat(ret.get(1).getSecret()).isEqualTo("secret2");
    }

    @Test
    public void retrieveByShopId3(){
        assertThrows(BusinessException.class,()->shopLogisticsDao.retrieveByShopId(19L,1,10));
    }

    @Test
    public void retrieveByShopIdInternal1(){
        List<ShopLogistics> ret=shopLogisticsDao.retrieveByShopIdInternal(1L);

        assertThat(ret.get(0).getId()).isEqualTo(1L);
        assertThat(ret.get(0).getLogisticsId()).isEqualTo(1L);
        assertThat(ret.get(0).getInvalid()).isEqualTo(ShopLogistics.VALID);
        assertThat(ret.get(0).getPriority()).isEqualTo(3);
        assertThat(ret.get(0).getSecret()).isEqualTo("secret1");
        assertThat(ret.get(1).getId()).isEqualTo(2L);
        assertThat(ret.get(1).getLogisticsId()).isEqualTo(2L);
        assertThat(ret.get(1).getInvalid()).isEqualTo(ShopLogistics.VALID);
        assertThat(ret.get(1).getPriority()).isEqualTo(113);
        assertThat(ret.get(1).getSecret()).isEqualTo("secret2");
    }

    @Test
    public void retrieveByShopIdInternal2(){
        assertThrows(BusinessException.class,()->shopLogisticsDao.retrieveByShopIdInternal(19L));
    }

    @Test
    public void saveTest(){
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);

        ShopLogistics shopLogistics=ShopLogistics.builder()
                .shopId(1L)
                .logisticsId(1L)
                .secret("secret2")
                .priority(1).build();
        ShopLogistics ret=shopLogisticsDao.save(shopLogistics,user);
        assertThat(ret.getPriority()).isEqualTo(1);
        assertThat(ret.getSecret()).isEqualTo("secret2");
        assertThat(ret.getLogisticsId()).isEqualTo(1L);
        assertThat(ret.getShopId()).isEqualTo(1L);
    }

    @Test
    public void insertTest(){
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);

        ShopLogistics shopLogistics=ShopLogistics.builder()
                .shopId(1L)
                .logisticsId(1L)
                .invalid(ShopLogistics.INVALID)
                .priority(1)
                .secret("secret1").build();

        ShopLogistics ret=shopLogisticsDao.insert(shopLogistics,user);
        assertThat(ret.getShopId()).isEqualTo(1L);
        assertThat(ret.getLogisticsId()).isEqualTo(1L);
        assertThat(ret.getInvalid()).isEqualTo(ShopLogistics.INVALID);
        assertThat(ret.getPriority()).isEqualTo(1);
        assertThat(ret.getSecret()).isEqualTo("secret1");

    }
}

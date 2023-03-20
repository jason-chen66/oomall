package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.FreightApplication;
import cn.edu.xmu.oomall.freight.FreightTestApplication;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.service.dto.SimpleLogisticsDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = FreightTestApplication.class)
@Transactional
public class LogisticsDaoTest {

    @MockBean
    private RedisUtil redisUtil;

    @Autowired
    private LogisticsDao logisticsDao;

    @Test
    public void findLogisticsByBillCode1(){
        List<SimpleLogisticsDto> ret=logisticsDao.findLogisticsByBillCode(null);

        assertThat(ret.get(0).getId()).isEqualTo(1L);
        assertThat(ret.get(0).getName()).isEqualTo("顺丰快递");
        assertThat(ret.get(1).getId()).isEqualTo(2L);
        assertThat(ret.get(1).getName()).isEqualTo("中通快递");
        assertThat(ret.get(2).getId()).isEqualTo(3L);
        assertThat(ret.get(2).getName()).isEqualTo("极兔速递");
    }

    @Test
    public void findLogisticsByBillCode2(){
        List<SimpleLogisticsDto> ret=logisticsDao.findLogisticsByBillCode("ZTO123456789123");

        assertThat(ret.get(0).getId()).isEqualTo(2L);
        assertThat(ret.get(0).getName()).isEqualTo("中通快递");
    }

    @Test
    public void findLogisticsByBillCode3(){
        assertThrows(BusinessException.class, ()-> logisticsDao.findLogisticsByBillCode("test"));
    }

    @Test
    public void findById1(){
        Logistics logistics=Logistics.builder().name("顺丰快递").appId("SF1001")
                .snPattern("^SF[A-Za-z0-9-]{4,35}$").logisticClass("sfDao").build();

        String key=String.format(LogisticsDao.KEY,1L);
        Mockito.when(redisUtil.hasKey(key)).thenReturn(true);
        Mockito.when(redisUtil.get(key)).thenReturn((Logistics) logistics);

        Logistics ret=logisticsDao.findById(1L);

        assertThat(ret.getName()).isEqualTo(logistics.getName());
        assertThat(ret.getAppId()).isEqualTo(logistics.getAppId());
        assertThat(ret.getSnPattern()).isEqualTo(logistics.getSnPattern());
        assertThat(ret.getLogisticsClass()).isEqualTo(logistics.getLogisticsClass());
    }

    @Test
    public void findById2(){
        String key=String.format(LogisticsDao.KEY,1L);
        Mockito.when(redisUtil.hasKey(key)).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Logistics ret=logisticsDao.findById(1L);

        assertThat(ret.getName()).isEqualTo("顺丰快递");
        assertThat(ret.getAppId()).isEqualTo("SF1001");
        assertThat(ret.getSnPattern()).isEqualTo("^SF[A-Za-z0-9-]{4,35}$");
    }

}

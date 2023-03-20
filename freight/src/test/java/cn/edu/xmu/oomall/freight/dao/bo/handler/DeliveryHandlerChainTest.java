package cn.edu.xmu.oomall.freight.dao.bo.handler;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.freight.FreightTestApplication;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.dao.openfeign.bo.SimpleRegion;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = FreightTestApplication.class)
@Transactional
public class DeliveryHandlerChainTest {
    @Autowired
    DeliveryHandlerChain deliveryHandlerChain;
    @MockBean
    RedisUtil redisUtil;

    @MockBean
    RegionDao regionDao;
    @Test
    public void doHandler(){
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        List<SimpleRegion> simpleRegionList= new ArrayList<>();
        simpleRegionList.add(SimpleRegion.builder().id(0L).build());
        InternalReturnObject<List<SimpleRegion>> internalReturnObject = new InternalReturnObject<>(simpleRegionList);
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setErrmsg(ReturnNo.OK.getMessage());
        Mockito.when(regionDao.getParentRegionsById(1L)).thenReturn(internalReturnObject);

        DeliveryInfo ret = deliveryHandlerChain.doHandler(1L,1L);
        assertThat(ret.getWarehouse().getId()).isEqualTo(1L);
        assertThat(ret.getShopLogistics().getId()).isEqualTo(1L);
    }
}

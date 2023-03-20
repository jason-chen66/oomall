package cn.edu.xmu.oomall.order.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.order.OrderApplication;
import cn.edu.xmu.oomall.order.OrderTestApplication;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.mapper.generator.OrderPoMapper;
import cn.edu.xmu.oomall.order.mapper.generator.po.OrderPo;
import cn.edu.xmu.oomall.order.service.dto.ConsigneeDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.criteria.Expression;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = OrderTestApplication.class)
@Transactional
public class OrderDaoTest {

    @MockBean
    private OrderPoMapper orderPoMapper;


    private final OrderDao orderDao;

    @MockBean
    private RedisUtil redisUtil;

    @Autowired
    public OrderDaoTest(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Test
    void findById1() {
        assertThrows(BusinessException.class, () -> orderDao.findById(null));
    }

    @Test
    void findById2() {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(orderPoMapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(null);
        assertThrows(BusinessException.class, () -> orderDao.findById(1L));
    }

    @Test
    void findById3() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderSn("2016102361242");

        Mockito.when(redisUtil.hasKey(String.format(OrderDao.KEY, 1))).thenReturn(true);
        Mockito.when(redisUtil.get(String.format(OrderDao.KEY, 1))).thenReturn(order);

        Order retOrder = orderDao.findById(1L);

        assertThat(retOrder.getId()).isEqualTo(order.getId());
        assertThat(retOrder.getOrderSn()).isEqualTo(order.getOrderSn());
    }

    @Test
    void findById4() {
        OrderPo orderPo = OrderPo.builder()
                .id(1L)
                .orderSn("2016102361242")
                .address("人民北路")
                .build();

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(orderPoMapper.selectByPrimaryKey(Mockito.anyLong())).thenReturn(orderPo);

        Order retOrder = orderDao.findById(1L);

        assertThat(retOrder.getId()).isEqualTo(orderPo.getId());
        assertThat(retOrder.getOrderSn()).isEqualTo(orderPo.getOrderSn());
    }

}

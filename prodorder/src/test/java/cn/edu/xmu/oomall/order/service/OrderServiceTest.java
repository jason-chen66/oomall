package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.oomall.order.OrderTestApplication;
import cn.edu.xmu.oomall.order.service.dto.StatusDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = OrderTestApplication.class)
@Transactional
public class OrderServiceTest {

    private OrderService orderService;

    @Autowired
    public OrderServiceTest(OrderService orderService) {
        this.orderService = orderService;
    }

    @Test
    public void retrieveOrderStates() {
        List<StatusDto> ret = orderService.retrieveOrderStates();
        assertEquals(12, ret.size());
    }
}

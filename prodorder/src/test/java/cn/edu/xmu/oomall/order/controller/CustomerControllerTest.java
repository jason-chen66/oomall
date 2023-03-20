package cn.edu.xmu.oomall.order.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.order.OrderTestApplication;
import cn.edu.xmu.oomall.order.dao.openfeign.GoodsDao;
import org.hamcrest.CoreMatchers;
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

import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = OrderTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class CustomerControllerTest {


    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private GoodsDao goodsDao;

    private static String adminToken;

    private static String ORDER_STATES_URL = "/orders/states";

    private static String ORDERS_URL = "/orders";

    private static String ORDERS_ID_URL = "/orders/{id}";

    private static String ORDERS_ID_PAY_URL = "/orders/{id}/pay";

    @Autowired
    public CustomerControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    @Test
    public void retrieveStates() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(ORDER_STATES_URL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(12))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void createOrder() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        String body = "{\"orderItems\": [{\"onsaleId\": 1, \"quantity\": 0, \"actId\": 501}],\"consignee\":{\"name\": \"彭世杰\",\"mobile\": \"18793828376\",\"regionId\": \"1092\",\"address\": \"南营子大街32号营业厅\"},\"message\": \"等待所有商品齐备后再发\"";
        this.mockMvc.perform(MockMvcRequestBuilders.post(ORDERS_URL)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void retrieveOrders1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        this.mockMvc.perform(MockMvcRequestBuilders.get(ORDERS_URL, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("orderSn", "2016102361242")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", CoreMatchers.is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '%d')].id", 1).value(1));
    }

    @Test
    public void retrieveOrders2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        this.mockMvc.perform(MockMvcRequestBuilders.get(ORDERS_URL, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("beginTime", "2016-10-23T12:07:00")
                        .param("endTime", "2016-10-23T12:52:26"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", CoreMatchers.is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '%d')].id", 1).value(1));
    }

    @Test
    public void findOrder() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.put(ORDERS_ID_URL, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", CoreMatchers.is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateOrder() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        String body = "\"consignee\": \"彭伍杰\",\"regionId\": \"1092\",\"address\": \"南营子大街32号营业厅\",\"mobile\": \"18793828376\"";

        this.mockMvc.perform(MockMvcRequestBuilders.put(ORDERS_ID_URL, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", CoreMatchers.is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteOrder1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(ORDERS_ID_URL, 0L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }

    @Test
    public void deleteOrder2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(ORDERS_ID_URL, 22133L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", CoreMatchers.is(ReturnNo.OK.getErrNo())));
    }

    @Test
    public void payOrder() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        String body = "{\"point: 0\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.delete(ORDERS_ID_PAY_URL, 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", CoreMatchers.is(ReturnNo.OK.getErrNo())));
    }
}

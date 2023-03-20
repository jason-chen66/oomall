package cn.edu.xmu.oomall.mockexpress.controller;

import cn.edu.xmu.oomall.mockexpress.MockExpressApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
@SpringBootTest(classes = MockExpressApplication.class)
@AutoConfigureMockMvc
public class SfControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void addOrder() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/sfexpress/std/service/EXP_RECE_CREATE_ORDER")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"orderSn\":\"123456789\",\"logisticsSn\":\"123456789\",\"logisticsCompany\":\"顺丰\"}"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void getOrder() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/sfexpress/std/service/EXP_RECE_SEARCH_ORDER_RESP")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"orderSn\":\"123456789\",\"logisticsSn\":\"123456789\",\"logisticsCompany\":\"顺丰\"}"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void cancelOrder() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/sfexpress/std/service/EXP_RECE_UPDATE_ORDER")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"orderSn\":\"123456789\",\"logisticsSn\":\"123456789\",\"logisticsCompany\":\"顺丰\"}"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void getRoute() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/sfexpress/std/service/EXP_RECE_SEARCH_ROUTES")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"orderSn\":\"123456789\",\"logisticsSn\":\"123456789\",\"logisticsCompany\":\"顺丰\"}"))
                .andDo(MockMvcResultHandlers.print());
    }
}

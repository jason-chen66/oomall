package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.FreightTestApplication;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = FreightTestApplication.class)
@Transactional
public class ExpressDaoTest {

    @Autowired
    private ExpressDao expressDao;

    @Test
    public void save()
    {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("TestExpress");
        userDto.setUserLevel(1);
        Express express = Express.builder()
                .shopLogisticsId(1L)
                .shopId(1L)
                .senderName("王大柱")
                .senderMobile("12345467890")
                .senderRegionId(1L)
                .senderAddress("桂花街桂花路")
                .deliveryName("李二蛋")
                .deliveryMobile("234567890")
                .deliveryRegionId(2L)
                .deliveryAddress("玫瑰路玫瑰街")
                .status(Express.UNSENT)
                .build();

        Express express1 = expressDao.insert(express,userDto);
        assertThat(express1.getShopId().equals(express.getShopId()));
        assertThat(express1.getId().equals(1L));
    }

}

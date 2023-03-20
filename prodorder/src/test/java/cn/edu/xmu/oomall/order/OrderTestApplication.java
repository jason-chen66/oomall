package cn.edu.xmu.oomall.order;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.javaee.core","cn.edu.xmu.oomall.order"})
@MapperScan("cn.edu.xmu.oomall.order.mapper.generator")
@EnableFeignClients
@EnableDiscoveryClient
@EnableScheduling
public class OrderTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}

package cn.edu.xmu.oomall.freight;

import cn.edu.xmu.oomall.freight.controller.InternalFreightController;
import cn.edu.xmu.oomall.freight.service.ExpressService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
@ComponentScan(basePackages = {"cn.edu.xmu.javaee.core", "cn.edu.xmu.oomall.freight"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = { SpringBootApplication.class })})
@MapperScan("cn.edu.xmu.oomall.freight.mapper.generator")
@SpringBootConfiguration
@EnableAutoConfiguration
@EnableFeignClients
@EnableDiscoveryClient
public class FreightTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(FreightTestApplication.class, args);
    }
}

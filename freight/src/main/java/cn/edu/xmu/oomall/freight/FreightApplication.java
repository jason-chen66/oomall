package cn.edu.xmu.oomall.freight;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Dasong Lu
 */
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.javaee.core", "cn.edu.xmu.oomall.freight"})
@MapperScan("cn.edu.xmu.oomall.freight.mapper.generator")
@EnableFeignClients
@EnableDiscoveryClient
public class FreightApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreightApplication.class, args);
    }

}

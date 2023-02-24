package cn.xf.basedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BaseDemoApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled", "false");
        SpringApplication.run(BaseDemoApplication.class, args);
    }

}

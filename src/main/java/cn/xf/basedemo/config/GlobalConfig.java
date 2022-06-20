package cn.xf.basedemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: xf-boot-base
 * @ClassName GlobalConfig
 * @description:
 * @author: xiongfeng
 * @create: 2022-06-20 17:05
 **/
@Data
@Component
@ConfigurationProperties(prefix = "global")
public class GlobalConfig {

    /**
     * rsa 公匙
     */
    private String rsaPublicKey;

    /**
     * rsa 私匙
     */
    private String rsaPrivateKey;
}

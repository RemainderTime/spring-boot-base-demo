package cn.xf.basedemo.config;


import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MqConfig
 *
 * @author 海言
 * @date 2025/10/24
 * @time 17:55
 * @Description rocketMq配置开关类
 */
@Configuration
@ConditionalOnProperty(prefix = "rocketmq", name = "enabled", havingValue = "true")
public class MqConfig {

    @Bean
    public RocketMQTemplate rocketMQTemplate() {
        // 配置生产者、消费者等
        return new RocketMQTemplate();
    }

}

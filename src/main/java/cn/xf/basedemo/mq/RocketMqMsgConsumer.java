package cn.xf.basedemo.mq;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * RocketMqMsgComsumer
 *
 * @author 海言
 * @date 2025/10/13
 * @time 14:37
 * @Description
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "user-topic",consumerGroup = "consumer-group")
public class RocketMqMsgConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        log.info("接收到消息---------：{}",s);
    }
}

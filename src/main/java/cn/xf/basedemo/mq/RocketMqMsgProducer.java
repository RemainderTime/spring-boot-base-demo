package cn.xf.basedemo.mq;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

/**
 * RocketMqMsgProducer
 *
 * @author 海言
 * @date 2025/10/13
 * @time 14:34
 * @Description
 */
@Slf4j
@Service
public class RocketMqMsgProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    //发送普通消息
    public void sendMsg(String topic, String msg) {
        rocketMQTemplate.convertAndSend(topic, msg);
        log.info("发送普通消息：{}", msg);
    }

    //发送带标签的消息
    public void sendMsg(String topic, String tag, String msg) {
        rocketMQTemplate.convertAndSend(topic + ":" + tag, msg);
    }

    //发送延迟消息
    public void sendDelayMsg(String topic, String msg, int delayLevel) {
        rocketMQTemplate.syncSendDelayTimeMills(topic, msg, delayLevel);
    }

}
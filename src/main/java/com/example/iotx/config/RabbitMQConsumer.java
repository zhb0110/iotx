package com.example.iotx.config;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;

/**
 * RabbitMQ 消息队列 消费监听回调
 */
@Slf4j
@Component
public class RabbitMQConsumer {

    private static RabbitMQConsumer rabbitMQConsumer;
    @Resource
    private WebSocketServerEndpoint webSocketServerEndpoint; //引入WebSocket
    // 可存对象方式
    @Autowired
    private RedisTemplate redisTemplate;
    // 存字符串方式
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 构造方法注入rabbitTemplate
     */
    @PostConstruct
    public void init() {
        rabbitMQConsumer = this;
        rabbitMQConsumer.webSocketServerEndpoint = webSocketServerEndpoint;
    }

    /**
     * 监听队列
     *
     * @param content
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = RabbitMQConfig.msg_queue)
    public void msgReceive(String content, Message message, Channel channel) throws IOException {
        log.info("----------------接收到消息--------------------" + content);

        // 解析json
        JSONObject msgObject = JSONObject.parseObject(content);

        // TODO:检查是否前端订阅，如果订阅，发送给WebSocket 由WebSocket推送给前端
        // 此处强制推送
        rabbitMQConsumer.webSocketServerEndpoint.sendMessageOnline(content);
        // TODO:识别是否是遥测，遥信数据，存放在redis中
        // 此处强制存放redis
        // HASH
        HashMap<String, String> map = new HashMap<>();
        map.put("COMMUNICATION_STATUS", "1"); // 设备在线
        for (String key : msgObject.keySet()) {
            map.put(key, String.valueOf(msgObject.get(key))); // 设备在线
        }
        stringRedisTemplate.opsForHash().putAll("CD__ZHGY_CH99", map);

        // 确认消息已接收
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}

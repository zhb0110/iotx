package com.example.iotx.mqttConfig2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * mqttv3通过配置来测试
 *
 * @Author Zhb
 * @create 2025/6/9 15:41
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MqttTest {

    @Autowired
    private MqttPublisher mqttPublisher;

    @Test
    public void MqttTest() throws Exception {
        mqttPublisher.publish("hello world");
        mqttPublisher.disconnect();
    }
}

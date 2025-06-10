package com.example.iotx.controller;

import com.example.iotx.mqttConfig2.MqttPublisher;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试Mqtt
 */
@RestController
@RequestMapping("/mqtt2")
public class TestMqtt2Controller {

    @Autowired
    private MqttPublisher mqttPublisher;

    /**
     * 消息发布和订阅测试--
     * 主题可以任意创建使用
     * 但只有配置的主题，服务端才会接收
     */
    @RequestMapping(value = "sendToMqtt", method = {RequestMethod.GET, RequestMethod.POST})
    public void mqttTest() throws MqttException {
//        mqttGateway.sendToMqtt("subTopic", "哈哈");
        mqttPublisher.publish("hello world");
//        mqttPublisher.disconnect();
    }
}

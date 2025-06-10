package com.example.iotx.controller;

import com.example.iotx.model.MsgObject;
import com.example.iotx.config.RabbitMQProduct;
import com.example.iotx.mqttConfig.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 测试消息队列和mqtt
 */
@RestController
@RequestMapping("/testMsgAndMqtt")
public class TesMsgAndMqttController {

    @Resource
    private RabbitMQProduct rabbitMQProduct;

    @Autowired
    private MqttGateway mqttGateway;

    /**
     * 发送消息测试--
     * 测试消息队列+websocket
     * 发送到生产端，消费端接收到消息，同时在消费端调用websocket发送到前端，并手动确认消息队列成功了
     */
    @RequestMapping(value = "sendMSG", method = {RequestMethod.GET, RequestMethod.POST})
    public void sendMSG() {
        rabbitMQProduct.testSendMSG(new MsgObject("设备1", "{\"height\":\"100\",\"width\":\"200\"}", "{\"state\":\"1\"}"));
    }

    /**
     * 消息发布和订阅测试--
     * 主题可以任意创建使用
     * 但只有配置的主题，服务端才会接收
     */
    @RequestMapping(value = "sendToMqtt", method = {RequestMethod.GET, RequestMethod.POST})
    public void mqttTest() {
        mqttGateway.sendToMqtt("subTopic", "哈哈");
    }



}


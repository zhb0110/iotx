package com.example.iotx.mqttConfig;

import org.eclipse.paho.client.mqttv3.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * mqttv3直接测试
 *
 * @Author Zhb
 * @create 2025/6/9 15:41
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MqttExample {

    @Test
    public void MqttExample() {
        String topic = "test/topic";
        String content = "Hello World";
        int qos = 2;
        String broker = "tcp://mqtt.eraworks.com:1883";
        String clientId = "JavaSample";

        try {
            // 创建客户端实例
            MqttClient client = new MqttClient(broker, clientId);

            // 连接选项
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName("dwy_mqtt"); // 设置用户名
            connOpts.setPassword("dwy@MQTT123".toCharArray()); // 设置密码

            // 设置回调
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            // 建立连接
            client.connect(connOpts);

            // 发布消息
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            client.publish(topic, message);

            // 断开连接
            client.disconnect();
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

}
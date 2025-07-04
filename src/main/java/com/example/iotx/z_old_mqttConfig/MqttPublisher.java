package com.example.iotx.z_old_mqttConfig;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

//@Configuration
public class MqttPublisher {

    @Value("${mqtt.config.hostUrl}")
    private String brokerUrl; // 替换为你的MQTT代理地址
    @Value("${mqtt.config.clientId}")
    private String clientId;
    @Value("${mqtt.config.pubTopic}")
    private String pubTopic; // 发布的主题
    @Value("${mqtt.config.qosLevel}")
    private int qosLevel = 1; // 设置默认的QoS级别
    @Value("${mqtt.config.retained}")
    private boolean retained = false; // 是否保留消息
    @Value("${mqtt.config.username}")
    private String username;
    @Value("${mqtt.config.password}")
    private String password;
    @Value("${mqtt.config.completionTimeout}")
    private int completionTimeout;
    @Value("${mqtt.config.keepAliveTime}")
    private int keepAliveTime;
    @Value("${mqtt.config.subTopic}")
    private String[] subTopic;

    private MqttClient client;


    @PostConstruct
    public void init() throws MqttException {
        if (clientId == null || clientId.isEmpty()) {
            throw new IllegalArgumentException("clientId must not be null or empty");
        }
        this.client = new MqttClient(brokerUrl, clientId);
        connect();
    }

    public MqttPublisher() throws MqttException {
//        this.client = new MqttClient(brokerUrl, clientId);
//        connect();
    }

    private void connect() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        // 可以在这里设置用户名密码等其他选项
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        //        设置连接超时时间
        options.setConnectionTimeout(completionTimeout);

        //        //设置连接间隔
//        GlobalVariables.MQTT_OPTIONS.setKeepAliveInterval(this.keepAliveTime.intValue());
//        GlobalVariables.MQTT_OPTIONS.setSocketFactory(SslUtil.getSocketFactory(this.caFile, this.certFile, this.keyFile, this.password));
//        GlobalVariables.MQTT_OPTIONS.setWill(topic, ("topic " + topicName + " closed").getBytes(), 2, true);
        // 设置连接间隔
        options.setKeepAliveInterval(keepAliveTime);
//        设置自动重连
        options.setAutomaticReconnect(true);
//        清除session
        options.setCleanSession(true);
//        使用秘钥
//        options.setSocketFactory(SslUtil.getSocketFactory());
//        设置遗嘱消息。qos=2: 精确一次（最可靠），retained 如果为 true，则服务器会保留该主题的最后一条消息，并在有新订阅者订阅该主题时立即发送给他们
//        暂时用不到
//        options.setWill(pubTopic, ("topic " + pubTopic + " closed").getBytes(), 2, true);


        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connection lost: " + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("Received message: " + new String(message.getPayload()));

                // 解析topic中的ID
                String[] parts = topic.split("/");
                if (parts.length == 4 && parts[1].equals("2001") && parts[3].equals("up")) {
                    String id = parts[2];
                    System.out.println("Received message for ID: " + id);
                    System.out.println("Message: " + new String(message.getPayload()));
                } else {
                    System.out.println("Received on unexpected topic: " + topic);
                }

//                // mqtt_receivedTopic
//                String topic = Objects.requireNonNull(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC)).toString();
//                log.info("订阅主题为: {}", topic);
//                String[] topics = mqttConfig.getSubTopic().split(",");
//
//                // TODO:模糊topic对应：格式：/2001/+/up
//                // 获得通道/网关标识
//                String channelName = topic.split("/").length > 0 ? topic.split("/")[topic.split("/").length - 2] : "";
//                JSONObject payload = JSONObject.parseObject(String.valueOf(message.getPayload()));
//                payload.put("channelName", channelName);
//                rabbitMQProduct.receiveDeviceMSG(payload);
//
//                // TODO:固定topic对应
////            for (String t : topics) {
////                if (t.equals(topic)) {
////                    if ("topic/devices/upload".equals(t)) {
////                        // 如果是topic/devices/upload，设备上传
////                        // 放入队列生产者
//////                        try {
////                        JSONObject payload = JSONObject.parseObject(String.valueOf(message.getPayload()));
//////                            String s_utf81 = new String(message.getPayload(), "UTF-8");
//////                            String s_utf8 = new String((byte[]) message.getPayload(), "UTF-8");
//////                            JSONObject payload = (JSONObject) message.getPayload();
////                        rabbitProduct.sendDeviceMSG(payload);
////
////                        log.info("接收到该主题消息为: {}", payload);
//////                        } catch (UnsupportedEncodingException e) {
//////                            throw new RuntimeException(e);
//////                        }
////
////
////                    } else if ("topic/devices/rpc/reply/+".equals(t)) {
////                        // TODO:略
////                        //  如果是，设备反馈
////                        // 放入队列生产者--可以用另一个方法
//////                        rabbitProduct.sendMSG(new Msg("设备1", "{\"height\":\"100\",\"width\":\"200\"}", "{\"state\":\"1\"}"));
////                    }
////
////
////                    log.info("接收到该主题消息为: {}", message.getPayload());
////                }
////            }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("Delivery complete: " + token.getMessageId());
            }
        });

        client.connect(options);
        System.out.println("Connected to the broker.");

///2001/+/up
//          模糊订阅
//        GlobalVariables.MQTT_CLIENT.subscribe((String[])topics.getValue0(), (int[])topics.getValue1()); //******444****mqttClient===>订阅*************
//        设置默认订阅，qos默认：0: 至多一次（可能丢失）
        client.subscribe(subTopic);

    }

    /**
     * 发布广播消息
     *
     * @param payload
     * @throws MqttException
     */
    public void publishTime(String payload) throws MqttException {
        MqttMessage message = new MqttMessage(LocalDateTime.now().toString().getBytes());
        message.setQos(qosLevel);
        message.setRetained(retained);

//        设置遗嘱消息。qos=2: 精确一次（最可靠），retained 如果为 true，则服务器会保留该主题的最后一条消息，并在有新订阅者订阅该主题时立即发送给他们
        client.publish(pubTopic, message); // 使用默认主题发布消息
        System.out.println("Published message: " + payload);
    }

    /**
     * 定制topic发布消息
     *
     * @param topic
     * @param payload
     * @throws MqttException
     */
    public void publish(String topic, String payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(qosLevel);
        message.setRetained(retained);

        client.publish(topic, message); // 使用指定主题发布消息
        System.out.println("Published message: " + payload);
    }

    public void setAsync(boolean async) {
        // 在这里根据async标志决定是否需要启用异步机制
        // 注意：Paho MQTT v3库本身没有直接提供异步事件API，通常通过回调函数实现异步处理
    }

    public void disconnect() throws MqttException {
        client.disconnect();
        System.out.println("Disconnected from the broker.");
    }

    // 设置回调以处理消息传递确认（如果需要）
    public void setCallback(MqttCallback callback) {
        client.setCallback(callback);
    }
}

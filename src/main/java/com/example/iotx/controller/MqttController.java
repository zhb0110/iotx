
package com.example.iotx.controller;

import com.example.iotx.mqttConfig.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mqtt")
public class MqttController {

    @Autowired
    private MqttService mqttService;

    @PostMapping("/publish")
    public String publish(@RequestParam String topic, @RequestParam String payload) {
        try {
            mqttService.publish(topic, payload);
            return "Message published successfully";
        } catch (Exception e) {
            return "Failed to publish message: " + e.getMessage();
        }
    }

    @PostMapping("/disconnect")
    public String disconnect() {
        try {
            mqttService.disconnect();
            return "Disconnected successfully";
        } catch (Exception e) {
            return "Failed to disconnect: " + e.getMessage();
        }
    }
}
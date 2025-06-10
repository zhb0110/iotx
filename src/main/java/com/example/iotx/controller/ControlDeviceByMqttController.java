package com.example.iotx.controller;

import com.example.iotx.model.RpcObject;
import com.example.iotx.mqttConfig.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
public class ControlDeviceByMqttController {

    @Autowired
    private MqttGateway mqttGateway;

    // 设备上报数据
    // 设备上传：
    //     服务端订阅：topic/devices/upload
    //     设备publish到topic：topic/devices/upload
    // 服务端反馈：
    //     设备订阅：topic/devices/reply
    //     服务端publish到topic：topic/devices/reply
    @RequestMapping(value = "upload", method = {RequestMethod.GET, RequestMethod.POST})
    public void mqttUpload() {
        mqttGateway.sendToMqtt("topic/devices/upload", "设备上传数据");
    }


    // 命令下发(只有订阅才能用通配符+或#)
    // 设备下发：
    //      设备订阅：topic/devices/rpc/+
    //      服务端publish：topic/devices/rpc/deviceName
    // 设备反馈：
    //      服务端订阅：topic/devices/rpc/reply/+
    //      设备publish：topic/devices/rpc/reply/deviceName
    @RequestMapping(value = "rpc_get", method = {RequestMethod.GET})
    public void mqttRpc(@RequestParam("controlValue") String controlValue) {
        mqttGateway.sendToMqtt("topic/devices/rpc/deviceName", "设备下发数据：" + controlValue);
        // 监听到设备的反馈则说明消息下发成功，但是经常不需要拿反馈，因为设备的数据会实时上报，这样就能直接看到效果
    }

    // 命令下发(只有订阅才能用通配符+或#)
    // 设备下发：
    //      设备订阅：topic/devices/rpc/+
    //      服务端publish：topic/devices/rpc/deviceName
    // 设备反馈：
    //      服务端订阅：topic/devices/rpc/reply/+
    //      设备publish：topic/devices/rpc/reply/deviceName
    @RequestMapping(value = "rpc_post", method = {RequestMethod.POST})
    public void mqttRpc(@RequestBody RpcObject rpcObject) {
        mqttGateway.sendToMqtt("topic/devices/rpc/deviceName", "设备下发数据：" + rpcObject.getControlValue());
        // 监听到设备的反馈则说明消息下发成功，但是经常不需要拿反馈，因为设备的数据会实时上报，这样就能直接看到效果
    }

    // 命令下发(只有订阅才能用通配符+或#)
    // 设备下发：
    //      设备订阅：topic/devices/rpc/+
    //      服务端publish：topic/devices/rpc/deviceName
    // 设备反馈：
    //      服务端订阅：topic/devices/rpc/reply/+
    //      设备publish：topic/devices/rpc/reply/deviceName
    @RequestMapping(value = "rpc_post_ems", method = {RequestMethod.POST})
    public void mqttRpcEMS(@RequestBody RpcObject rpcObject) {
        mqttGateway.sendToMqtt("/2001/abc/down", "设备下发数据：" + rpcObject.getControlValue());
        // 监听到设备的反馈则说明消息下发成功，但是经常不需要拿反馈，因为设备的数据会实时上报，这样就能直接看到效果
    }

}

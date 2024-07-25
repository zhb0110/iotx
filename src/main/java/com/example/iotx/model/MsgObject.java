package com.example.iotx.model;

import lombok.Data;

@Data
public class MsgObject {

    // 设备名
    String deviceName = "";

    // 属性
    String attributes = "";

    // 实时值
    String telemetry = "";


    public MsgObject(String deviceName, String attributes, String telemetry) {
        this.deviceName = deviceName;
        this.attributes = attributes;
        this.telemetry = telemetry;
    }
}

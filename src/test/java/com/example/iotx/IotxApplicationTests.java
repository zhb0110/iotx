package com.example.iotx;

import com.alibaba.fastjson.JSONObject;
import com.example.iotx.config.Msg;
import com.example.iotx.config.RabbitProduct;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class IotxApplicationTests {

//    @Resource
//    private RabbitProduct rabbitConsultProduct;

    @Test
    void contextLoads() {
    }

    @Test
    void testJSON() {
        Msg msg = new Msg("设备1", "{height:100,width:200}", "{state:1}");

        System.out.println(JSONObject.toJSON(msg).toString());
    }

}

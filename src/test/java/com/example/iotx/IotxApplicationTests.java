package com.example.iotx;

import com.alibaba.fastjson.JSONObject;
import com.example.iotx.model.MsgObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IotxApplicationTests {

//    @Resource
//    private RabbitProduct rabbitConsultProduct;

    @Test
    void contextLoads() {
    }

    @Test
    void testJSON() {
        MsgObject msgObject = new MsgObject("设备1", "{height:100,width:200}", "{state:1}");

        System.out.println(JSONObject.toJSON(msgObject).toString());
    }

}

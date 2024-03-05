package com.example.iotx.controller;

import com.example.iotx.mqttConfig.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 测试线程池
 */
@RestController
@RequestMapping("/device")
public class TestThreadPoolController {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    // 场景1：定时任务--定时开关设备等，也可以用延迟消息队列来操作
    // 可结合xxl-job quartz
    // TODO:常用的集合和多线程使用~

    // 执行
//    @Autowired
//    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
//        threadPoolTaskExecutor.execute(() ->service.xxx(param));

    // 30秒后执行开关设备
    // 0.5秒间隔一个命令
    @RequestMapping("/rpcDevices")
    public void rpcDevice() {
        // 守护线程，做什么用?用处不大，比如说保持心跳这类的，它不能持有资源，可能会让资源异常
        ScheduledExecutorService ses = new ScheduledThreadPoolExecutor(4);

//        ScheduledExecutorService ses = Executors.newScheduledThreadPool(4);

        // 解析脚本，执行下嘛的任务

        // 1秒后执行一次性任务:
        ses.schedule(new Task("one-time1"), 1, TimeUnit.SECONDS);
        ses.schedule(new Task("one-time2"), 2, TimeUnit.SECONDS);
        ses.schedule(new Task("one-time3"), 3, TimeUnit.SECONDS);
        ses.schedule(new Task("one-time4"), 4, TimeUnit.SECONDS);
//        // 2秒后开始执行定时任务，每3秒执行:---固定周期执行，即使上一个没执行完成
//        ses.scheduleAtFixedRate(new Task("fixed-rate"), 2, 3, TimeUnit.SECONDS);
//        // 2秒后开始执行定时任务，以3秒为间隔执行:--上一个执行完成后，固定延迟执行，有可能执行不到次数
//        ses.scheduleWithFixedDelay(new Task("fixed-delay"), 2, 3, TimeUnit.SECONDS);

//        threadPoolTaskExecutor.execute(() -> openDevice());
//        threadPoolTaskExecutor.execute(() -> closeDevive());
    }

    //    开设备
    private void openDevice() {

    }

    //    关设备
    private void closeDevive() {

    }


    // 场景2：下载进度--进度条

    // 场景3：日志清理


}

class Task implements Runnable {
    private final String name;

    @Autowired
    private MqttGateway mqttGateway;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("start task " + name);
//        try {
//            Thread.sleep(1000);
//            下发topic
        mqttGateway.sendToMqtt("topic/devices/rpc/deviceName", "设备下发数据");

//        } catch (InterruptedException e) {
//        }
        System.out.println("end task " + name);
    }
}

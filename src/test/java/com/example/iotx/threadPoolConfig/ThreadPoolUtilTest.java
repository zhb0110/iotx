package com.example.iotx.threadPoolConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author Zhb
 * @create 2024/7/26 下午5:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ThreadPoolUtilTest {

    @Autowired
    private ThreadPoolUtil threadPoolUtil;

    @Test
    public void run() throws Exception {
        for (int i = 0; i < 10; i++) {
            final int taskNum = i;
            threadPoolUtil.execute(() -> {
                System.out.println("Task " + taskNum + " is running on thread " + Thread.currentThread().getName());
            });
        }
    }
}

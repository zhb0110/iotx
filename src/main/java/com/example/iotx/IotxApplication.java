package com.example.iotx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * mapper增加配置，否则无法扫描到，可以单独做一个MybatisPlusConfigurer
 */
@SpringBootApplication
@MapperScan("com.example.iotx.mapper")
//@MapperScan("com.example.**.mapper") 也可以模糊对应的方式
public class IotxApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(IotxApplication.class, args);
    }

    private static final Integer MAIN_THREAD_POOL_SIZE = 4;

    // TODO:固定数量的线程池
    private final ExecutorService executor = Executors.newFixedThreadPool(MAIN_THREAD_POOL_SIZE,
            new ThreadFactoryImpl("Demo_TestThread_", false));

    // TODO:加载一些配置和启动项
    @Override
    public void run(String... args) throws Exception {
//        Thread loopThread = new Thread(new LoopThread());
//        loopThread.start();
    }

    public class LoopThread implements Runnable {
        @Override
        public void run() {
            // TODO:启动一个线程去监听队列中的消息，一但消息投递到队列中，我们就取出消息然后异步多线程处理该消息
            for (int i = 0; i < MAIN_THREAD_POOL_SIZE; i++) {
                executor.execute(() -> {
                    while (true) {
                        //取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到
//                        try {
////                            NettyMsgModel nettyMsgModel = QueueHolder.get().take();
////                            messageProcessor.process(nettyMsgModel);
//                        } catch (InterruptedException e) {
////                            log.error(e.getMessage(), e);
//                        }
                    }
                });
            }
        }
    }
}

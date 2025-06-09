package com.example.iotx.threadPoolConfig;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author Zhb
 * @create 2024/7/26 下午5:48
 */
public class ThreadPoolUtil {
    private ThreadPoolTaskExecutor taskExecutor;

    public ThreadPoolUtil(int corePoolSize, int maxPoolSize, int queueCapacity, int keepAliveSeconds) {
//        taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setCorePoolSize(corePoolSize);
//        taskExecutor.setMaxPoolSize(maxPoolSize);
//        taskExecutor.setQueueCapacity(queueCapacity);
//        taskExecutor.initialize();

        taskExecutor = new ThreadPoolTaskExecutor();
        //  配置核心线程数--推荐设置10
        taskExecutor.setCorePoolSize(corePoolSize);
        //  配置最大线程数--推荐设置200
        taskExecutor.setMaxPoolSize(maxPoolSize);
        //  配置队列大小 --推荐设置1200
        taskExecutor.setQueueCapacity(queueCapacity);
        /**
         * 线程池维护线程所允许的空闲时间--单位秒，超过销毁
         * 线程池线程数量大于corePoolSize时候，多出来的空闲线程，多长时间会被销毁
         */
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        //  配置线程池中的线程的名称前缀
        taskExecutor.setThreadNamePrefix("pool-send-task-executor");
        /**
         * TODO:线程池拒绝策略，
         * ThreadPoolExecutor.AbortPolicy 默认，丢弃任务并抛出RejectedExecutionException异常
         * ThreadPoolExecutor.DiscardPolicy：丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新提交被拒绝的任务
         * ThreadPoolExecutor.CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
         */
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();

        System.out.println("线程池初始化......");
    }

    public void ThreadPoolUtil2(int corePoolSize, int maxPoolSize, int queueCapacity, int keepAliveSeconds) {

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1000);
    }

        public void execute(Runnable task) {
        taskExecutor.execute(task);
    }
}

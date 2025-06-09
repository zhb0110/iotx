package com.example.iotx.z_old_threadPoolConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 自定义线程池的配置类
 */
@Configuration
@EnableAsync
public class TaskExecutePool {

//    @Value("${task.executor.core_pool_size}")
//    private int corePoolSize;
//    @Value("${task.executor.max_pool_size}")
//    private int maxPoolSize;
//    @Value("${task.executor.queue_capacity}")
//    private int queueCapacity;
//    @Value("${task.executor.keep_alive_seconds}")
//    private int keepAliveSeconds;
//
//    @Bean(name = "threadPoolTaskExecutor")
//    public ThreadPoolTaskExecutor SendTaskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        //  配置核心线程数--推荐设置10
//        executor.setCorePoolSize(corePoolSize);
//        //  配置最大线程数--推荐设置200
//        executor.setMaxPoolSize(maxPoolSize);
//        //  配置队列大小 --推荐设置1200
//        executor.setQueueCapacity(queueCapacity);
//        /**
//         * 线程池维护线程所允许的空闲时间--单位秒，超过销毁
//         * 线程池线程数量大于corePoolSize时候，多出来的空闲线程，多长时间会被销毁
//         */
//        executor.setKeepAliveSeconds(keepAliveSeconds);
//        //  配置线程池中的线程的名称前缀
//        executor.setThreadNamePrefix("pool-send-task-executor");
//        /**
//         * TODO:线程池拒绝策略，
//         * ThreadPoolExecutor.AbortPolicy 默认，丢弃任务并抛出RejectedExecutionException异常
//         * ThreadPoolExecutor.DiscardPolicy：丢弃任务，但是不抛出异常。
//         * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新提交被拒绝的任务
//         * ThreadPoolExecutor.CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
//         */
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        executor.initialize();
//
//        return executor;
//
//    }
}

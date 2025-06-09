package com.example.iotx.threadPoolConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Zhb
 * @create 2024/7/26 下午5:50
 */
@Configuration
@ConfigurationProperties(prefix = "threadpool")
public class ThreadPoolConfig {

    @Value("${threadpool.core_pool_size}")
    private int corePoolSize;

    @Value("${threadpool.max_pool_size}")
    private int maxPoolSize;

    @Value("${threadpool.queue_capacity}")
    private int queueCapacity;

    @Value("${threadpool.keep_alive_seconds}")
    private int keepAliveSeconds;

    @Bean
    public ThreadPoolUtil threadPoolUtil() {
        return new ThreadPoolUtil(corePoolSize, maxPoolSize, queueCapacity,keepAliveSeconds);
    }
}

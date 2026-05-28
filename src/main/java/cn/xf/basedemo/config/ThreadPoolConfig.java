package cn.xf.basedemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description: 统一异步线程池配置类（防止默认线程池由于无界队列引发 OOM）
 * @Author: xiongfeng
 * @Date: 2026/5/28
 * @Version: 1.0
 */
@Slf4j
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean(name = "asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        log.info("初始化异步线程池 [asyncServiceExecutor] 开始...");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 核心线程数：根据 CPU 核心数与业务性质配置（此处设为 10）
        executor.setCorePoolSize(10);
        // 最大线程数：高并发时能够支持的最大线程数
        executor.setMaxPoolSize(50);
        // 缓冲队列容量：限制任务队列大小，防止无界队列堆积导致 OOM
        executor.setQueueCapacity(1000);
        // 空闲线程存活时间（秒）
        executor.setKeepAliveSeconds(60);
        // 线程名称前缀
        executor.setThreadNamePrefix("async-executor-");

        // 拒绝策略：当队列满且达到最大线程数时，由调用者线程直接执行任务（保证任务不丢失，同时降低提交速度）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 优雅停机配置
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();
        log.info("初始化异步线程池 [asyncServiceExecutor] 成功！");
        return executor;
    }
}

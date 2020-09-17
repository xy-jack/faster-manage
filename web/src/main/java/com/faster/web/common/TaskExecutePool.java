package com.faster.web.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 全局线程池配置
 *
 * @author da.yang@hand-china.com
 * @date 2020/8/19
 */
@Configuration
public class TaskExecutePool {



    /**
     * corePoolSize: 指定了线程池中的线程数量，它的数量决定了添加的任务是开辟新的线程去执行，还是放到workQueue任务队列中去, 核心线程会一直存活，及时没有任务需要执行
     * 当线程数小于核心线程数时，即使有线程空闲，线程池也会优先创建新线程处理
     * 设置allowCoreThreadTimeout=true（默认false）时，核心线程会超时关闭
     * maximumPoolSize:指定了线程池中的最大线程数量，这个参数会根据你使用的workQueue任务队列的类型，决定线程池会开辟的最大线程数量；
     * keepAliveTime:当线程池中空闲线程数量超过corePoolSize时，多余的线程会在多长时间内被销毁；
     * unit:keepAliveTime的单位
     * workQueue:任务队列，被添加到线程池中，但尚未被执行的任务；它一般分为直接提交队列、有界任务队列、无界任务队列、优先任务队列几种；
     * threadFactory:线程工厂，用于创建线程，一般用默认即可；
     * handler:拒绝策略；当任务太多来不及处理时，如何拒绝任务；
     */
    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setQueueCapacity(1000);
        taskExecutor.setThreadNamePrefix("");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return taskExecutor;
    }

}

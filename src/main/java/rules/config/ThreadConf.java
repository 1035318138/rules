package rules.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadConf {


    @Value("${threadPool.keepAliveSeconds:60}")
    private Integer keepAliveSeconds;
    @Value("${threadPool.queueCapacity:10}")
    private Integer queueCapacity;
    @Value("${threadPool.corePoolSize:10}")
    private Integer corePoolSize;

    @Primary
    @Bean
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //此方法返回可用处理器的虚拟机的最大数量; 不小于1
        int core = Runtime.getRuntime().availableProcessors();
        //在K8S上会拿到只有1
        if (core == 1) {
            core = corePoolSize;
        }
        //设置核心线程数
        executor.setCorePoolSize(core);
        //设置最大线程数
        executor.setMaxPoolSize(core * 2 - 1);
        //除核心线程外的线程存活时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        //如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
        executor.setQueueCapacity(queueCapacity);
        //线程名称前缀
        executor.setThreadNamePrefix("async-");
        //设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

}

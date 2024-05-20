package rules.controller;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import threadLocal.Test;
import threadLocal.utils.AESUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Can.Ru
 */

public class ThreadLocalController {

    private static ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    static {
        //此方法返回可用处理器的虚拟机的最大数量; 不小于1
        int core = Runtime.getRuntime().availableProcessors();
        //设置核心线程数
        executor.setCorePoolSize(core);
        //设置最大线程数
        executor.setMaxPoolSize(5);

        //如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
        executor.setQueueCapacity(10);
        //线程名称前缀
        executor.setThreadNamePrefix("async-");
        //重试添加当前的任务，自动重复调用 execute() 方法，直到成功
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //确保应用最后能够被关闭，而不是阻塞住
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
    }

    @RequestMapping("/demo")
    public Boolean demo(){
        for (int i=0;i<10;i++){

            int finalI = i;
            //CompletableFuture.runAsync(()-> Test.excute(finalI),executor);

        }
        return true;
    }




}

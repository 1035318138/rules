package rules.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Component
public class ThreadPoolUtil {


    @Autowired
    private ThreadPoolTaskExecutor executor;



    public <T> void asyncRun(Consumer<T> c){
        CompletableFuture.runAsync(()->{
            c.accept(null);
        },executor);
    }
}

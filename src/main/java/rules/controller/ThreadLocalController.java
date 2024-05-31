package rules.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import threadLocal.Test;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Can.Ru
 */
@RequestMapping("/thread")
@RestController
public class ThreadLocalController {


    @Autowired
    private ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();


    @RequestMapping("/demo1")
    public Boolean demo1() {
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            CompletableFuture.runAsync(() -> Test.excute(finalI), executor);

        }
        return true;
    }


    @Autowired
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @RequestMapping("/fixedThread")
    public String decode() {
        for (int i = 0; i < 1010; i++) {
            int j = i * 1000;

            CompletableFuture.runAsync(() -> {
                System.out.println(new Date().toString() + "====" + j);
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }, executorService);

        }
        return "ok";
    }


}

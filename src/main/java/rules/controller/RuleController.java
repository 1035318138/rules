package rules.controller;

import org.easyrules.api.RulesEngine;
import org.easyrules.core.RulesEngineBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rules.MyRule;
import rules.RuleService;
import rules.config.RuleConf;
import threadLocal.Test;
import threadLocal.utils.AESUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Can.Ru
 */
@RestController
@RequestMapping("/rule")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @RequestMapping("/demo")
    public Boolean demo(){
        // 创建规则对象
        MyRule myRule = new MyRule();

        boolean condition = ruleService.condition("demoService", "getTrue");

        // 设置规则条件
        myRule.setCondition(true); // 触发规则执行

        // 创建规则引擎
        RulesEngine rulesEngine = RulesEngineBuilder.aNewRulesEngine().build();

        // 注册规则
        rulesEngine.registerRule(myRule);

        // 执行规则
        rulesEngine.fireRules();
        return condition;
    }


    @RequestMapping("/conf")
    public String conf(){

        return staticConf();
    }

    private static String staticConf() {
        return RuleConf.key1;
    }




    @Autowired
    private  ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();


    @RequestMapping("/demo1")
    public Boolean demo1(){
        for (int i=0;i<20;i++){
            int finalI = i;
            CompletableFuture.runAsync(()-> Test.excute(finalI),executor);

        }
        return true;
    }

    @Autowired
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @RequestMapping("/decode")
    public String decode(){
        for (int i = 0; i < 1010; i++) {
            int j =i * 1000;

            CompletableFuture.runAsync(() -> {
                System.out.println(new Date().toString() +"===="+j);
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }, executorService);

        }
        return "s";
    }
}

package rules.controller;

import org.apache.commons.logging.LogFactory;
import org.easyrules.api.RulesEngine;
import org.easyrules.core.RulesEngineBuilder;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
import java.util.concurrent.*;

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

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    // 模拟库存数量
    private static final String STOCK_KEY = "seckill:stock";
    // Redis 分布式锁的 key
    private static final String LOCK_KEY = "seckill:lock:";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @RequestMapping("/flashSale")
    public String flashSale(){

        //假设入参商品为mock1
        String productCode = "mock1";
        RLock lock = redissonClient.getLock(LOCK_KEY + productCode);
        try {
            boolean isLock = lock.tryLock(100, 10000, TimeUnit.MILLISECONDS);
            if (isLock) {
                try {
                    Long stock = redisTemplate.opsForValue().decrement("rules:flashSale:mock1");
                    if (stock != null && stock >= 0) {
                        namedParameterJdbcTemplate.getJdbcTemplate().execute("update test.stock set stock=stock-1 where id =1");
                        return "扣减成功";
                    } else {
                        return "库存不足";
                    }
                } finally {
                    lock.unlock();
                }


            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "未获取锁";
    }
}

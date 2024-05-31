package rules.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/lock")
public class LockController {


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

    // Redis 分布式锁的 key
    private static final String LOCK_KEY = "seckill:lock:";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @RequestMapping("/flashSale")
    public String flashSale() {

        //假设入参商品为mock1
        String productCode = "mock1";
        RLock lock = redissonClient.getLock(LOCK_KEY + productCode);
        try {
            boolean isLock = lock.tryLock(100, 10000, TimeUnit.MILLISECONDS);
            if (isLock) {
                try {
                    //原子性操作每次都减一 确保锁的颗粒度最小化 最终会出现redis库存为负,数据库为0
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

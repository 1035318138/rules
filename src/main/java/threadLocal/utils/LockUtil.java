package threadLocal.utils;

import jodd.util.StringUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Component
public class LockUtil {


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * redisson 有看门狗机制,更符合需求,锁定到完成或者挂掉
     *
     * @param key
     * @param consumer
     * @param <T>
     */
    public <T> void tryLockOnceWithRedisson(String key, Consumer<T> consumer) {

        tryLockOnceWithRedisson(key, consumer, null);
    }

    /**
     * redisson 有看门狗机制,锁定到完成或者挂掉
     *
     * @param key
     * @param consumer
     * @param <T>
     */
    public <T> void tryLockOnceWithRedisson(String key, Consumer<T> consumer, Consumer<T> onFinishBeforeUnLock) {

        RLock lock = redissonClient.getLock(key);
        Boolean success = false;
        try {
            success = lock.tryLock();
            if (success) {
                consumer.accept(null);
            } else {
                System.out.println(String.format("加锁失败:%s", key));
            }
        } catch (Exception e) {
            System.out.println(String.format("加锁异常:%s", e));
        } finally {
            if (success) {
                if (onFinishBeforeUnLock != null) {
                    onFinishBeforeUnLock.accept(null);
                }
                lock.unlock();
            }
        }
    }

    /**
     * 尝试锁定,不成功就拉倒
     *
     * @param redisLockKey
     * @param waitTime
     * @param leaseTime
     * @param consumer
     * @param <T>
     */
    public <T> void tryLockOnce(String redisLockKey, long waitTime, long leaseTime, Consumer<T> consumer) {
        boolean res = false;
        RLock lock = redissonClient.getLock(redisLockKey);

        try {
            res = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
            if (res) {
                consumer.accept(null);
            }
        } catch (Exception e) {
            System.out.println(String.format("tryLockOnce Exception:%s", e));
        } finally {
            if (res) {
                lock.unlock();
            }
        }
    }

    /**
     * key-1 然后key-1后的值
     *
     * @param key
     * @return
     */
    public Long decrement(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void set(String key, String value, Integer day) {
        redisTemplate.opsForValue().set(key, value, day, TimeUnit.DAYS);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    public Integer getInteger(String key) {
        String v = redisTemplate.opsForValue().get(key);
        if(StringUtil.isNotBlank(v)){
            return Integer.valueOf(v);
        }
        return null;
    }

    /**
     * key列表数量不大时使用
     *
     * @param key
     * @return
     */
    public Set<String> getKeys(String key) {
        return redisTemplate.keys(key + "*");
    }

}

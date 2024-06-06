package rules.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rules.utils.LockUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/lock")
public class LockController {


    @Autowired
    private LockUtil lockUtil;

    // Redis 分布式锁的 key
    private static final String LOCK_KEY = "seckill:lock:";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 利用分布式锁 秒杀锁库存
     * 压测脚本resource/jmx/flashSale.jmx
     * @return
     */
    @RequestMapping("/flashSale")
    public String flashSale() {

        //假设入参商品为mock1
        String productCode = "mock1";

        AtomicReference<Boolean> res = new AtomicReference<>();
        lockUtil.tryLockOnce(LOCK_KEY + productCode, 100, 10000, obj -> {

            //原子性操作每次都减一 确保锁的颗粒度最小化 最终会出现redis库存为负,数据库为0
            Long stock = lockUtil.decrement("rules:flashSale:mock1");
            if (stock != null && stock >= 0) {
                namedParameterJdbcTemplate.getJdbcTemplate().execute("update test.stock set stock=stock-1 where id =1");
                res.set(true);
            } else {
                res.set(false);
            }
        });

        return res.get() == null ? "未获取锁" : res.get() ? "下单成功" : "库存不足";
    }

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 上面代码简写利用分布式锁 秒杀锁库存
     * 压测脚本resource/jmx/flashSale.jmx
     * @return
     */
    @RequestMapping("/flashSaleV2")
    public String flashSaleV2() {
        //假设入参商品为mock1
        String productCode = "mock1";

        boolean isLock = false;
        RLock lock = redissonClient.getLock(LOCK_KEY + productCode);
        try {
            isLock = lock.tryLock(100, 10000, TimeUnit.MILLISECONDS);
            if (isLock) {
                Integer stock = lockUtil.getInteger("rules:flashSale:mock1");
                if (stock != null && stock >= 1) {
                    lockUtil.decrement("rules:flashSale:mock1");
                    namedParameterJdbcTemplate.getJdbcTemplate().execute("update test.stock set stock=stock-1 where id =1");
                    return "下单成功";
                } else {
                    return "库存不足";
                }
            }
            return "未获取锁";

        } catch (Exception e) {
            System.out.println(String.format("flashSaleV2 Exception:%s", e));
        } finally {
            if (isLock) {
                lock.unlock();
            }
        }
        return "下单异常";
    }
}

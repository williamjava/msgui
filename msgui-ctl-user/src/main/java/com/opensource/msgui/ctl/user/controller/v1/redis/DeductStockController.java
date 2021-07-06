package com.opensource.msgui.ctl.user.controller.v1.redis;

import com.opensource.msgui.commons.response.ResponseResult;
import com.opensource.msgui.manager.redis.api.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author whj
 *
 * Reids模拟秒杀系统扣减库存逻辑
 */
@RestController
@RequestMapping("/api/redis/v1/stock")
public class DeductStockController {
    private final String stockKey = "stock";
    @Resource
    private RedisService redisService;

    @Resource
    private Redisson redisson;

    /**
     * 初始化库存
     * @return
     */
    @PostMapping("deduct/initStock")
    public ResponseResult initStock(Integer stockNum) {
        redisService.set("stock", stockNum);

        return ResponseResult.success("初始化库存成功");
    }

    /**
     * 剩余库存
     * @return
     */
    @GetMapping("deduct/currStock")
    public ResponseResult currStock() {
        return ResponseResult.success("剩余库存：" + redisService.get("stock"));
    }

    /**
     * 正常扣减库存逻辑
     * @return
     */
    @PostMapping("deduct/normal")
    public ResponseResult deductNormal() {
        int stock = Integer.parseInt(redisService.get(stockKey));
        if (stock > 0) {
            int remainingStock = stock - 1;
            redisService.set(stockKey, remainingStock);

            System.out.println("扣减成功，剩余库存：" + remainingStock);
            return ResponseResult.success("扣减成功，剩余库存：" + remainingStock);
        } else {
            System.out.println("库存不足，扣减失败");
            return ResponseResult.success("库存不足，扣减失败");
        }
    }

    /**
     * 扣减库存(使用setnx实现分布式锁)
     * @return
     */
    @PostMapping("deduct/normalUsingSetnx")
    public ResponseResult deductNormalUsingSetnx() {
        String lockKey = "product_001";

        /**
         * 通过setnx获取一把锁
         */
        String clientId = UUID.randomUUID().toString();
        Boolean result = redisService.setIfAbsent(lockKey, clientId, 10, TimeUnit.SECONDS);
        if (!result) {
            System.out.println("系统繁忙，请稍后重试");
            return ResponseResult.failure("系统繁忙，请稍后重试");
        }

        try {
            int stock = Integer.parseInt(redisService.get(stockKey));
            if (stock > 0) {
                int remainingStock = stock - 1;
                redisService.set(stockKey, remainingStock);

                System.out.println("扣减成功，剩余库存：" + remainingStock);
                return ResponseResult.success("扣减成功，剩余库存：" + remainingStock);
            } else {
                System.out.println("库存不足，扣减失败");
                return ResponseResult.success("库存不足，扣减失败");
            }
        } finally {
            if (clientId.equals(redisService.get(lockKey))) {
                redisService.del(lockKey);
            }
        }
    }

    /**
     * 扣减库存(使用分布式锁Redisson，让原本并行的请求串行了，高并发场景，性能差(可以使用分段锁思想提高性能))
     * @return
     */
    @PostMapping("deduct/normalUsingRedisson")
    public ResponseResult deductNormalUsingRedisson() {
        String lockKey = "product_001";

        //获取一把Redisson锁
        RLock redissonLock = redisson.getLock(lockKey);
        try {
            //加锁
            redissonLock.lock();
            int stock = Integer.parseInt(redisService.get(stockKey));
            if (stock > 0) {
                int remainingStock = stock - 1;
                redisService.set(stockKey, remainingStock);

                System.out.println("扣减成功，剩余库存：" + remainingStock);
                return ResponseResult.success("扣减成功，剩余库存：" + remainingStock);
            } else {
                System.out.println("库存不足，扣减失败");
                return ResponseResult.success("库存不足，扣减失败");
            }
        } finally {
            redissonLock.unlock();
        }
    }

    /**
     * 分布式锁Redisson之读锁 (用于解决读多写少场景的缓存数据库双写不一致)
     * @return
     */
    @PostMapping("deduct/readStock")
    public ResponseResult readStock(Long clientId) throws InterruptedException {
        String lockKey = "product_001";

        RReadWriteLock readWriteLock = redisson.getReadWriteLock(lockKey);
        RLock readLock = readWriteLock.readLock();

        readLock.lock();
        System.out.println("获取读锁成功：client=" + clientId);
        String stock = redisService.get(stockKey);
        if (StringUtils.isEmpty(stock)) {
            System.out.println("查询数据库库存为200");
            Thread.sleep(5000);
            redisService.set(stockKey, 200);
        }
        readLock.unlock();
        System.out.println("释放读锁成功：client=" + clientId);

        return ResponseResult.success();
    }

    /**
     * 分布式锁Redisson之写锁(用于解决读多写少场景的缓存数据库双写不一致)
     * @return
     */
    @PostMapping("deduct/writeStock")
    public ResponseResult writeStock(Long clientId) throws InterruptedException {
        String lockKey = "product_001";

        RReadWriteLock readWriteLock = redisson.getReadWriteLock(lockKey);
        RLock writeLock = readWriteLock.writeLock();

        writeLock.lock();
        System.out.println("获取写锁成功：client=" + clientId);
        System.out.println("修改商品001的数据库库存为500");
        redisService.del(stockKey);
        Thread.sleep(5000);
        writeLock.unlock();
        System.out.println("释放写锁成功：client=" + clientId);

        return ResponseResult.success();
    }
}

    

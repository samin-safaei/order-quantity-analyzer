package com.cleanhub.orderquantityanalyzer.infrastructure.scheduler;

import com.cleanhub.orderquantityanalyzer.domain.scheduler.TaskLockService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TaskLockRepository implements TaskLockService {

    private final StringRedisTemplate redisTemplate;

    public TaskLockRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean tryLock(String lockKey, long lockExpirationTimeOut, TimeUnit unit) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        Boolean acquired = ops.setIfAbsent(lockKey, "locked", lockExpirationTimeOut, unit);
        return Boolean.TRUE.equals(acquired);
    }

    public void unlock(String lockKey) {
        redisTemplate.delete(lockKey);
    }
}

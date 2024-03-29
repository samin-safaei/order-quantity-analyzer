package com.cleanhub.orderquantityanalyzer.application.scheduler;

import com.cleanhub.orderquantityanalyzer.domain.scheduler.TaskLockService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TaskLockApplicationService {

    private final TaskLockService taskLockService;

    private final long defaultLockExpirationTimeOut;

    public TaskLockApplicationService(TaskLockService taskLockService,
                                      @Value("${order-quantity-analyzer.scheduler.default-lock-expiration-timeout-seconds}")
                                      long defaultLockExpirationTimeOut) {
        this.taskLockService = taskLockService;
        this.defaultLockExpirationTimeOut = defaultLockExpirationTimeOut;
    }

    public boolean tryLock(String lockKey) {
        return taskLockService.tryLock(lockKey, defaultLockExpirationTimeOut, TimeUnit.MILLISECONDS);
    }

    public void unlock(String lockKey) {
        taskLockService.unlock(lockKey);
    }

}

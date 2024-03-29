package com.cleanhub.orderquantityanalyzer.domain.scheduler;

import java.util.concurrent.TimeUnit;

public interface TaskLockService {

    boolean tryLock(String lockKey, long lockExpirationTimeOut, TimeUnit unit);

    void unlock(String lockKey);

}

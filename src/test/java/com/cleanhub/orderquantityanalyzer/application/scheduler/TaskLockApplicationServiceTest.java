package com.cleanhub.orderquantityanalyzer.application.scheduler;

import com.cleanhub.orderquantityanalyzer.domain.scheduler.TaskLockService;
import com.cleanhub.orderquantityanalyzer.testutil.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class TaskLockApplicationServiceTest extends AbstractIntegrationTest {

    private TaskLockApplicationService underTest;
    @Mock
    private TaskLockService mockTaskLockService;


    @Value("${order-quantity-analyzer.scheduler.default-lock-expiration-timeout-seconds}")
    private long defaultLockTimeOut;

    @BeforeEach
    void setUp() {
        underTest = new TaskLockApplicationService(mockTaskLockService, defaultLockTimeOut);
    }

    @Test
    void testTryLock() {
        String lockKey = "testLock";

        // Configure the mock to return true when trying to acquire the lock
        when(mockTaskLockService.tryLock(eq(lockKey), eq(defaultLockTimeOut), eq(TimeUnit.MILLISECONDS))).thenReturn(true);

        // Call the method under test
        boolean result = underTest.tryLock(lockKey);

        // Verify the interaction with the mock and the returned result
        verify(mockTaskLockService).tryLock(eq(lockKey), eq(defaultLockTimeOut), eq(TimeUnit.MILLISECONDS));
        assert(result); // Assuming you want to assert that the lock was acquired successfully
    }

    @Test
    void testUnlock() {
        // WHEN
        String lockKey = "testLock";

        // Call the unlock method
        underTest.unlock(lockKey);

        // Verify that the unlock method was called on the mock
        verify(mockTaskLockService).unlock(lockKey);
    }
}

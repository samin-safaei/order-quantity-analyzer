package com.cleanhub.orderquantityanalyzer.infrastructure.scheduler;

import com.cleanhub.orderquantityanalyzer.testutil.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
public class TaskLockRepositoryTest extends AbstractIntegrationTest {
    private final TaskLockRepository taskLockRepository;

    @Autowired
    public TaskLockRepositoryTest(TaskLockRepository taskLockRepository) {
        this.taskLockRepository = taskLockRepository;
    }

    @Test
    public void testLockingMechanism() {
        // Assuming you have a method to set Redis properties dynamically
        // or you configure Redis host and port in your test properties
        assertTrue(taskLockRepository.tryLock("testLock", 10, TimeUnit.SECONDS), "Lock should be acquired");
        taskLockRepository.unlock("testLock");
    }

    @Test
    public void testConcurrentLockAcquisition() throws InterruptedException {
        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    latch.await(); // Ensure all threads start at the same time
                    boolean acquired = taskLockRepository.tryLock("concurrentTestLock", 10, TimeUnit.SECONDS);
                    if (acquired) {
                        successCount.incrementAndGet();
                        taskLockRepository.unlock("concurrentTestLock");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        latch.countDown(); // Start all waiting threads
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(1, successCount.get(), "Only one thread should successfully acquire the lock");
    }
}
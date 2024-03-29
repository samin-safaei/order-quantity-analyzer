package com.cleanhub.orderquantityanalyzer.presentation.scheduler;

import com.cleanhub.orderquantityanalyzer.application.customer.CustomerApplicationService;
import com.cleanhub.orderquantityanalyzer.application.scheduler.TaskLockApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerRetrievalSchedulerTest {

    private CustomerRetrievalScheduler underTest;
    private final CustomerApplicationService mockCustomerApplicationService = Mockito.mock();
    private final TaskLockApplicationService mockTaskLockApplicationService = Mockito.mock();
    private final String customerRetrievalLockKey = "randomLockKey";

    @BeforeEach
    void setUp() {
        underTest = new CustomerRetrievalScheduler(mockCustomerApplicationService, mockTaskLockApplicationService, customerRetrievalLockKey);
    }

    @Test
    void execute_shouldAcquireLockAndProceedCustomerRetrievalAsExpected() {
        // WHEN
        when(mockTaskLockApplicationService.tryLock(eq(customerRetrievalLockKey))).thenReturn(true);
        underTest.executeCustomerRetrievalTask();

        // THEN
        verify(mockTaskLockApplicationService).tryLock(eq(customerRetrievalLockKey));
        verify(mockCustomerApplicationService).fetchAndSaveNewCustomers();
        verify(mockTaskLockApplicationService).unlock(eq(customerRetrievalLockKey));
    }

    @Test
    void execute_shouldNotExecuteWhenLockIsNotAvailable() {
        // WHEN
        when(mockTaskLockApplicationService.tryLock(eq(customerRetrievalLockKey))).thenReturn(false);
        underTest.executeCustomerRetrievalTask();

        // THEN
        verify(mockTaskLockApplicationService).tryLock(eq(customerRetrievalLockKey));
        verify(mockCustomerApplicationService, never()).fetchAndSaveNewCustomers();
        verify(mockTaskLockApplicationService, never()).unlock(eq(customerRetrievalLockKey));
    }
}
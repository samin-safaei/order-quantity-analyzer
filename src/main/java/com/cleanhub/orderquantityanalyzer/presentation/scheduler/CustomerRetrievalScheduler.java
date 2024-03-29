package com.cleanhub.orderquantityanalyzer.presentation.scheduler;

import com.cleanhub.orderquantityanalyzer.application.customer.CustomerApplicationService;
import com.cleanhub.orderquantityanalyzer.application.scheduler.TaskLockApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CustomerRetrievalScheduler {

    private final CustomerApplicationService customerApplicationService;
    private final TaskLockApplicationService taskLockApplicationService;
    private final String customerRetrievalLockKey;

    public CustomerRetrievalScheduler(CustomerApplicationService customerApplicationService,
                                      TaskLockApplicationService taskLockApplicationService,
                                      @Value("${order-quantity-analyzer.scheduler.customer-retrieval-lock-key}")
                                      String customerRetrievalLockKey) {
        this.customerApplicationService = customerApplicationService;
        this.taskLockApplicationService = taskLockApplicationService;
        this.customerRetrievalLockKey = customerRetrievalLockKey;
    }

    @Scheduled(fixedRate = 86400, initialDelay = 20, timeUnit = TimeUnit.SECONDS)
    public void executeCustomerRetrievalTask() {
        logger.info("Started customer retrieval task");
        if (taskLockApplicationService.tryLock(customerRetrievalLockKey)) {
            try {
                customerApplicationService.fetchAndSaveNewCustomers();
            } finally {
                taskLockApplicationService.unlock(customerRetrievalLockKey);
            }
        }
    }

}

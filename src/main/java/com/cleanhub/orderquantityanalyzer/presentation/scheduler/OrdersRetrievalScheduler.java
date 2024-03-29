package com.cleanhub.orderquantityanalyzer.presentation.scheduler;

import com.cleanhub.orderquantityanalyzer.application.order.OrderApplicationService;
import com.cleanhub.orderquantityanalyzer.application.scheduler.TaskLockApplicationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Component
public class OrdersRetrievalScheduler {

    private final OrderApplicationService orderApplicationService;
    private final TaskLockApplicationService taskLockApplicationService;
    private final String orderRetrievalLockKey;

    public OrdersRetrievalScheduler(OrderApplicationService orderApplicationService,
                                    TaskLockApplicationService taskLockApplicationService,
                                    @Value("${order-quantity-analyzer.scheduler.order-retrieval-lock-key}")
                                      String orderRetrievalLockKey) {
        this.orderApplicationService = orderApplicationService;
        this.taskLockApplicationService = taskLockApplicationService;
        this.orderRetrievalLockKey = orderRetrievalLockKey;
    }

    @Scheduled(fixedRate = 120, initialDelay = 60, timeUnit = TimeUnit.SECONDS)
    public void executeCustomerRetrievalTask() {
        if (taskLockApplicationService.tryLock(orderRetrievalLockKey)) {
            try {
                orderApplicationService.retrieveAndSaveOrders();
            } finally {
                taskLockApplicationService.unlock(orderRetrievalLockKey);
            }
        }
    }

}

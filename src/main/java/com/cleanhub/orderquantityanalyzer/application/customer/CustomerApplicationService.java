package com.cleanhub.orderquantityanalyzer.application.customer;

import com.cleanhub.orderquantityanalyzer.domain.customer.Customer;
import com.cleanhub.orderquantityanalyzer.domain.customer.CustomerManager;
import com.cleanhub.orderquantityanalyzer.domain.customer.CustomerRetrievalService;
import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.exceptions.WebClientRetrievalException;
import io.micrometer.core.instrument.Counter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomerApplicationService {

    private final CustomerRetrievalService customerRetrievalService;
    private final CustomerManager customerManager;
    private final Counter customerRetrievalSuccessCounter;
    private final Counter customerRetrievalFailureCounter;
    private final int customerLimit;

    public CustomerApplicationService(CustomerRetrievalService customerRetrievalService,
                                      CustomerManager customerManager,
                                      Counter customerRetrievalSuccessCounter,
                                      Counter customerRetrievalFailureCounter,
                                      @Value("${order-quantity-analyzer.customer-retrieval.customer-limit}")
                                      int customerLimit) {
        this.customerRetrievalService = customerRetrievalService;
        this.customerManager = customerManager;
        this.customerRetrievalSuccessCounter = customerRetrievalSuccessCounter;
        this.customerRetrievalFailureCounter = customerRetrievalFailureCounter;
        this.customerLimit = customerLimit;
    }

    public void fetchAndSaveNewCustomers() {
        List<Customer> fetchedCustomers;
        try {
            logger.info("Started fetching customers");
            fetchedCustomers = customerRetrievalService.getAll();
            logger.info("Number fetched customers:{}", fetchedCustomers.size());
            customerRetrievalSuccessCounter.increment(); // Increment success counter
        } catch (WebClientRetrievalException e) {
            logger.error("Error while fetching customers:{}", e.getMessage());
            customerRetrievalFailureCounter.increment(); // Increment failure counter
            return; // Early return on failure to prevent null passed to saveNewCustomers
        }
        logger.info("Customer limit:{}", customerLimit);
        if (customerLimit > 0) {
            customerManager.saveNewCustomers(fetchedCustomers.subList(0, customerLimit));
        } else {
            customerManager.saveNewCustomers(fetchedCustomers);
        }
    }
}

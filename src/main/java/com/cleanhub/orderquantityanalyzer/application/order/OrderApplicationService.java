package com.cleanhub.orderquantityanalyzer.application.order;

import com.cleanhub.orderquantityanalyzer.domain.customer.Customer;
import com.cleanhub.orderquantityanalyzer.domain.customer.CustomerManager;
import com.cleanhub.orderquantityanalyzer.domain.order.Order;
import com.cleanhub.orderquantityanalyzer.domain.order.OrderManager;
import com.cleanhub.orderquantityanalyzer.domain.order.OrderRetrievalService;
import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.exceptions.WebClientRetrievalException;
import io.micrometer.core.instrument.Counter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class OrderApplicationService {

    private final OrderRetrievalService orderRetrievalService;
    private final CustomerManager customerManager;
    private final Counter orderRetrievalSuccessCounter;
    private final Counter orderRetrievalFailureCounter;
    private final OrderManager orderManager;

    public void retrieveAndSaveOrders() {
        List<Customer> allCustomers = customerManager.findAll();
        logger.info("Number of customers to fetch orders for: {}", allCustomers.size());

        allCustomers.forEach(customer -> {
            Order retrievedOrder;
            try {
                retrievedOrder = orderRetrievalService.getOrderByCustomer(customer);
                orderRetrievalSuccessCounter.increment();
            } catch (WebClientRetrievalException e) {
                logger.error("Error while fetching orders for customer {}:{}", customer.companyName(), e.getMessage());
                orderRetrievalFailureCounter.increment(); // Increment failure counter
                return; // Early return on failure to prevent null passed to saveNewCustomers
            }
            orderManager.saveOrder(retrievedOrder);
        });
    }

}

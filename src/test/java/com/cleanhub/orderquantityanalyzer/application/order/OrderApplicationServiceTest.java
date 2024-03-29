package com.cleanhub.orderquantityanalyzer.application.order;

import com.cleanhub.orderquantityanalyzer.domain.customer.Customer;
import com.cleanhub.orderquantityanalyzer.domain.customer.CustomerManager;
import com.cleanhub.orderquantityanalyzer.domain.order.Order;
import com.cleanhub.orderquantityanalyzer.domain.order.OrderManager;
import com.cleanhub.orderquantityanalyzer.domain.order.OrderRetrievalService;
import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.exceptions.WebClientRetrievalException;
import com.cleanhub.orderquantityanalyzer.testutil.TestCustomerFactory;
import com.cleanhub.orderquantityanalyzer.testutil.TestOrderFactory;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderApplicationServiceTest {

    private final OrderRetrievalService mockOrderRetrievalService = mock(OrderRetrievalService.class);
    private final CustomerManager mockCustomerManager = mock(CustomerManager.class);
    private final OrderManager mockOrderManager = mock(OrderManager.class);
    private OrderApplicationService underTest;
    private Counter successCounter;
    private Counter failureCounter;

    @BeforeEach
    void setUp() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        successCounter = meterRegistry.counter("order.retrieval.success");
        failureCounter = meterRegistry.counter("order.retrieval.failure");

        underTest = new OrderApplicationService(mockOrderRetrievalService, mockCustomerManager, successCounter, failureCounter, mockOrderManager);
    }

    @Test
    void fetchAndSaveNewCustomers_Success() {
        // GIVEN
        List<Customer> customers = TestCustomerFactory.getCustomers();
        Order expectedOrder = TestOrderFactory.get();
        when(mockCustomerManager.findAll()).thenReturn(customers);
        when(mockOrderRetrievalService.getOrderByCustomer(any())).thenReturn(expectedOrder);

        // WHEN
        underTest.retrieveAndSaveOrders();

        // THEN
        assertEquals(customers.size(), successCounter.count());
        verify(mockOrderRetrievalService, times(customers.size())).getOrderByCustomer(any());
        verify(mockOrderManager, times(customers.size())).saveOrder(eq(expectedOrder));
    }

    @Test
    void fetchAndSaveNewCustomers_Failure() {
        // GIVEN
        List<Customer> customers = TestCustomerFactory.getCustomers();
        when(mockCustomerManager.findAll()).thenReturn(customers);
        doThrow(new WebClientRetrievalException("")).when(mockOrderRetrievalService).getOrderByCustomer(any());

        // WHEN
        underTest.retrieveAndSaveOrders();

        // THEN
        assertEquals(customers.size(), failureCounter.count());
        verify(mockOrderRetrievalService, times(customers.size())).getOrderByCustomer(any());
        verify(mockOrderManager, never()).saveOrder(any());
    }
}
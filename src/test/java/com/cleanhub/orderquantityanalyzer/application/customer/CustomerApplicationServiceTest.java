package com.cleanhub.orderquantityanalyzer.application.customer;

import com.cleanhub.orderquantityanalyzer.domain.customer.Customer;
import com.cleanhub.orderquantityanalyzer.domain.customer.CustomerManager;
import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.exceptions.WebClientRetrievalException;
import com.cleanhub.orderquantityanalyzer.domain.customer.CustomerRetrievalService;
import com.cleanhub.orderquantityanalyzer.testutil.TestCustomerFactory;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerApplicationServiceTest {

    private final CustomerRetrievalService mockCustomerRetrievalService = mock(CustomerRetrievalService.class);
    private final CustomerManager mockCustomerManager = mock(CustomerManager.class);
    private final int customerLimit = 10;
    private CustomerApplicationService service;
    private Counter successCounter;
    private Counter failureCounter;

    @BeforeEach
    void setUp() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        successCounter = meterRegistry.counter("customer.retrieval.success");
        failureCounter = meterRegistry.counter("customer.retrieval.failure");

        service = new CustomerApplicationService(mockCustomerRetrievalService, mockCustomerManager, successCounter, failureCounter, customerLimit);
    }

    @Test
    void fetchAndSaveNewCustomers_Success() {
        // GIVEN
        List<Customer> expectedCustomer = TestCustomerFactory.getCustomersWithoutId();
        List<Customer> expectedLimitedCustomers = expectedCustomer.subList(0, customerLimit);
        when(mockCustomerRetrievalService.getAll()).thenReturn(expectedCustomer);

        // WHEN
        service.fetchAndSaveNewCustomers();

        // THEN
        assertEquals(1, successCounter.count());
        verify(mockCustomerManager, times(1)).saveNewCustomers(eq(expectedLimitedCustomers));
    }

    @Test
    void fetchAndSaveNewCustomers_Failure() {
        // GIVEN
        doThrow(new WebClientRetrievalException("")).when(mockCustomerRetrievalService).getAll();

        // WHEN
        service.fetchAndSaveNewCustomers();

        // THEN
        assertEquals(1, failureCounter.count());
        verify(mockCustomerManager, never()).saveNewCustomers(anyList());
    }
}

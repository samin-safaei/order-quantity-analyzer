package com.cleanhub.orderquantityanalyzer.domain.service;

import com.cleanhub.orderquantityanalyzer.domain.customer.Customer;
import com.cleanhub.orderquantityanalyzer.domain.customer.CustomerConverterService;
import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.exceptions.WebClientRetrievalException;
import com.cleanhub.orderquantityanalyzer.domain.customer.CustomerRetrievalService;
import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.customerLogo.CustomerLogo;
import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.customerLogo.CustomerLogoRetrievalService;
import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.exceptions.CustomerLogosRetrievalException;
import com.cleanhub.orderquantityanalyzer.testutil.TestCustomerLogoFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerRetrievalServiceTest {

    private CustomerRetrievalService underTest;

    private CustomerLogoRetrievalService mockCustomerLogoRetrievalService;
    private CustomerConverterService mockCustomerConverterService;


    @BeforeEach
    void setUp() {
        // Setup mock
        mockCustomerLogoRetrievalService = Mockito.mock(CustomerLogoRetrievalService.class);
        mockCustomerConverterService = Mockito.mock(CustomerConverterService.class);
        underTest = new CustomerRetrievalService(mockCustomerLogoRetrievalService, mockCustomerConverterService);
    }

    @Test
    void getAll() throws WebClientRetrievalException {
        // GIVEN
        List<CustomerLogo> expectedCustomerLogos = TestCustomerLogoFactory.getCustomerLogos();
        List<Customer> expectedCustomers = new ArrayList<>();
        for (CustomerLogo expectedCustomerLogo : expectedCustomerLogos) {
            expectedCustomers.add(new Customer(null, expectedCustomerLogo.companyName(), expectedCustomerLogo.landingPageRoute()));
        }

        // WHEN
        Mockito.when(mockCustomerLogoRetrievalService.getCustomers()).thenReturn(expectedCustomerLogos);
        Mockito.when(mockCustomerConverterService.fromCustomerLogos(expectedCustomerLogos)).thenReturn(expectedCustomers);
        List<Customer> actualCustomers = underTest.getAll();

        // THEN
        assertNotNull(actualCustomers);
        assertEquals(expectedCustomers.size(), actualCustomers.size());
        for (int i = 0; i < expectedCustomers.size(); i++) {
            assertEquals(expectedCustomers.get(i), actualCustomers.get(i));
        }
    }

    @Test
    void getAllShouldThrowCustomerRetrievalException() throws CustomerLogosRetrievalException {
        // GIVEN
        CustomerLogosRetrievalException customerLogosRetrievalException = new CustomerLogosRetrievalException("General Failure");

        // WHEN
        Mockito.when(mockCustomerLogoRetrievalService.getCustomers()).thenThrow(customerLogosRetrievalException);

        // THEN
        assertThrows(WebClientRetrievalException.class, () -> underTest.getAll());
    }
}
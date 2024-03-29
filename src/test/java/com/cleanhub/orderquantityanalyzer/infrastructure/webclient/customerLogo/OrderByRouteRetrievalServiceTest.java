package com.cleanhub.orderquantityanalyzer.infrastructure.webclient.customerLogo;

import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.exceptions.CustomerLogosRetrievalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class OrderByRouteRetrievalServiceTest {

    private CustomerLogosWebClient mockWebClient;
    private CustomerLogoRetrievalService underTest;

    @BeforeEach
    void setUp() {
        // Setup mock
        mockWebClient = Mockito.mock(CustomerLogosWebClient.class);
        underTest = new CustomerLogoRetrievalService(mockWebClient);
    }


    @Test
    void getCustomersSuccess() throws CustomerLogosRetrievalException {
        // Arrange
        List<CustomerLogo> expectedCustomers = List.of(new CustomerLogo("Company One", "/company-one"));
        Mockito.when(mockWebClient.fetchCustomers()).thenReturn(Flux.fromIterable(expectedCustomers));

        // Act
        List<CustomerLogo> actualCustomerLogos = underTest.getCustomers();

        // Assert
        assertEquals(expectedCustomers, actualCustomerLogos);
    }

    @Test
    void getCustomersTimeout() {
        // Arrange
        Mockito.when(mockWebClient.fetchCustomers()).thenReturn(Flux.never());

        // Act & Assert
        assertThrows(CustomerLogosRetrievalException.class, () -> underTest.getCustomers());
    }

    @Test
    void getCustomersWebClientResponseException() {
        // Arrange
        int expectedStatusCode = 404;
        WebClientResponseException mockException = WebClientResponseException.create(expectedStatusCode,
                "Not Found", null, null, null);
        Mockito.when(mockWebClient.fetchCustomers()).thenReturn(Flux.error(mockException));

        // Act & Assert
        CustomerLogosRetrievalException exception =
                assertThrows(CustomerLogosRetrievalException.class, () -> underTest.getCustomers());
        assertEquals("Error from external service: 404", exception.getMessage());
        assertEquals(expectedStatusCode, exception.getStatusCode());
    }

    @Test
    void getCustomersGeneralException() {
        // Arrange
        Mockito.when(mockWebClient.fetchCustomers()).thenReturn(Flux.error(new RuntimeException("Unexpected error")));

        // Act & Assert
        Exception exception = assertThrows(CustomerLogosRetrievalException.class, () -> underTest.getCustomers());
        assertEquals("General error occurred while fetching customers", exception.getMessage());
    }

}
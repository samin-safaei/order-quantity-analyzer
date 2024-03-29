package com.cleanhub.orderquantityanalyzer.infrastructure.webclient.customerLogo;

import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.exceptions.CustomerLogosRetrievalException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerLogoRetrievalService {

    private final CustomerLogosWebClient customerLogosWebClient;

    public List<CustomerLogo> getCustomers() throws CustomerLogosRetrievalException {
        return customerLogosWebClient.fetchCustomers()
                .timeout(Duration.ofMillis(500)) // Set a reasonable timeout
                .onErrorMap(this::mapToCustomException) // Convert WebClient exceptions to your custom exception
                .subscribeOn(Schedulers.boundedElastic()) // Use boundedElastic to offload blocking operation
                .collectList() // Collect Flux to List
                .block(); // Block until complete, with handling for TimeoutException
    }

    private CustomerLogosRetrievalException mapToCustomException(Throwable e) {
        // Here, map different types of WebClient exceptions to more meaningful exceptions for your context
        // This is a simple example; you can make it as complex as needed
        if (e instanceof WebClientResponseException) {
            int statusCode = ((WebClientResponseException) e).getStatusCode().value();
            return new CustomerLogosRetrievalException(statusCode, "Error from external service: " + statusCode, e);
        } else {
            return new CustomerLogosRetrievalException("General error occurred while fetching customers", e);
        }
    }

}

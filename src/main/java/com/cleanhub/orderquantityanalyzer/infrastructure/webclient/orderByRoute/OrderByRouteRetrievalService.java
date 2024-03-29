package com.cleanhub.orderquantityanalyzer.infrastructure.webclient.orderByRoute;

import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.exceptions.OrderByRouteRetrievalException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Service
@AllArgsConstructor
public class OrderByRouteRetrievalService {

    private final OrderByRouteWebClient orderByRouteWebClient;

    public OrderByRoute getOrder(String landingPageRoute) throws OrderByRouteRetrievalException {
        return orderByRouteWebClient.fetchOrdersByRoute(landingPageRoute)
                .timeout(Duration.ofMillis(500)) // Set a reasonable timeout
                .onErrorMap(e -> mapToCustomException(e, landingPageRoute)) // Convert WebClient exceptions to your custom exception
                .subscribeOn(Schedulers.boundedElastic()) // Use boundedElastic to offload blocking operation
                .block(); // Block until complete, with handling for TimeoutException
    }

    private OrderByRouteRetrievalException mapToCustomException(Throwable e, String landingPageRoute) {
        // Here, map different types of WebClient exceptions to more meaningful exceptions for your context
        // This is a simple example; you can make it as complex as needed
        if (e instanceof WebClientResponseException) {
            int statusCode = ((WebClientResponseException) e).getStatusCode().value();
            return new OrderByRouteRetrievalException(landingPageRoute, statusCode, "Error from external service: " + statusCode, e);
        } else {
            return new OrderByRouteRetrievalException(landingPageRoute, "General error occurred while fetching customers", e);
        }
    }

}

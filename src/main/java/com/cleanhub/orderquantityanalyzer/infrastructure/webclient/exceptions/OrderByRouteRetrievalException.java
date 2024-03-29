package com.cleanhub.orderquantityanalyzer.infrastructure.webclient.exceptions;

import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.exceptions.WebClientRetrievalException;

public class OrderByRouteRetrievalException extends WebClientRetrievalException {

    private String landingPageRoute;
    public OrderByRouteRetrievalException(String landingPageRoute, String message) {
        super(message);
        this.landingPageRoute = landingPageRoute;
    }

    public OrderByRouteRetrievalException(String landingPageRoute, String message, Throwable cause) {
        super(message, cause);
        this.landingPageRoute = landingPageRoute;
    }

    public OrderByRouteRetrievalException(String landingPageRoute, int statusCode, String message, Throwable cause) {
        super(statusCode, message, cause);
        this.landingPageRoute = landingPageRoute;
    }
}
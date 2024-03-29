package com.cleanhub.orderquantityanalyzer.infrastructure.webclient.exceptions;

public class CustomerLogosRetrievalException extends WebClientRetrievalException {
    public CustomerLogosRetrievalException(String message) {
        super(message);
    }

    public CustomerLogosRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerLogosRetrievalException(int statusCode, String message, Throwable cause) {
        super(statusCode, message, cause);
    }
}
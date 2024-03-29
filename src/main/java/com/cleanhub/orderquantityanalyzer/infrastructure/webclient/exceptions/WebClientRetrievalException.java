package com.cleanhub.orderquantityanalyzer.infrastructure.webclient.exceptions;

import lombok.Getter;

public class WebClientRetrievalException extends RuntimeException {

    @Getter
    private int statusCode;

    public WebClientRetrievalException(String message) {
        super(message);
    }

    public WebClientRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebClientRetrievalException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}

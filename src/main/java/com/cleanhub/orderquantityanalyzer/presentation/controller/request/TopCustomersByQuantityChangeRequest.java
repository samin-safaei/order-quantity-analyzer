package com.cleanhub.orderquantityanalyzer.presentation.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TopCustomersByQuantityChangeRequest(
        int pageNumber,
        int numberOfCompanies,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime fromDateTime,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime toDateTime) {
}

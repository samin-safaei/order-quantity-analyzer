package com.cleanhub.orderquantityanalyzer.application.orderHistory;

import java.time.LocalDateTime;

public record OrderHistoryDetailParams(int pageNumber,
                                       int numberOfCustomers,
                                       LocalDateTime fromDateTime,
                                       LocalDateTime toDateTime) {
}

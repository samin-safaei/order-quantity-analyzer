package com.cleanhub.orderquantityanalyzer.domain.order.orderHistory;

import java.time.LocalDateTime;

public record TopOrderHistoryByChangeQueryParams(int pageNumber,
                                                 int numberOfCompanies,
                                                 LocalDateTime fromDateTime,
                                                 LocalDateTime toDateTime) {
}

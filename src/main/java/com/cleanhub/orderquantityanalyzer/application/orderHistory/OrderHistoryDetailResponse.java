package com.cleanhub.orderquantityanalyzer.application.orderHistory;

import java.util.UUID;

public record OrderHistoryDetailResponse(UUID customerId,
                                         UUID orderId,
                                         String companyName,
                                         String landingPageRoute,
                                         Double latestQuantity,
                                         Double quantityChange) {
}

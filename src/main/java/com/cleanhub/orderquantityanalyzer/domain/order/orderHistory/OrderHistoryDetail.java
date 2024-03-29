package com.cleanhub.orderquantityanalyzer.domain.order.orderHistory;

import java.util.UUID;

public record OrderHistoryDetail(UUID customerId,
                                 UUID orderId,
                                 String companyName,
                                 String landingPageRoute,
                                 Double latestQuantity,
                                 Double quantityChange) {
}

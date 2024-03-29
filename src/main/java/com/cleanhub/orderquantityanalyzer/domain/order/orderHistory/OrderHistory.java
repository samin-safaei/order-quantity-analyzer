package com.cleanhub.orderquantityanalyzer.domain.order.orderHistory;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderHistory(UUID orderId, UUID customerId, String companyName, Double latestQuantity,
                           Double quantityChange, LocalDateTime createdAt) {
}

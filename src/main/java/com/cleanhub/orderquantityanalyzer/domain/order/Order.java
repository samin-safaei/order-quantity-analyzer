package com.cleanhub.orderquantityanalyzer.domain.order;

import java.util.UUID;

public record Order(UUID id, UUID customerId, String companyName, Double quantity) {
}

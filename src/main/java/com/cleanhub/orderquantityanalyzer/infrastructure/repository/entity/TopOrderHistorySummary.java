package com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity;

import java.util.UUID;

public record TopOrderHistorySummary(UUID customerId, Double sumQuantityChange) {

}

package com.cleanhub.orderquantityanalyzer.domain.customer;

import java.util.UUID;

public record Customer(UUID id, String companyName, String landingPageRoute) {
}

package com.cleanhub.orderquantityanalyzer.infrastructure.webclient.orderByRoute;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderByRoute(UUID uuid, String companyName, Double quantity) {
}

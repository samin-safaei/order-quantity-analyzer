package com.cleanhub.orderquantityanalyzer.infrastructure.webclient.customerLogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CustomerLogo(String companyName, String landingPageRoute) {

}

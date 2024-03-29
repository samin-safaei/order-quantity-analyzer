package com.cleanhub.orderquantityanalyzer.infrastructure.webclient.customerLogo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class CustomerLogosWebClient {

    private final WebClient webClient;
    private final String customersUri;

    @Autowired
    public CustomerLogosWebClient(WebClient.Builder webClientBuilder,
                                  @Value("${cleanhub.base.url}") String baseUrl,
                                  @Value("${cleanhub.logos.uri}") String customersUri) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.customersUri = customersUri;
    }

    public Flux<CustomerLogo> fetchCustomers() {
        return this.webClient.get()
                .uri(customersUri)
                .retrieve()
                .bodyToFlux(CustomerLogo.class);
    }

}

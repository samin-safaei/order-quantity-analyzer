package com.cleanhub.orderquantityanalyzer.infrastructure.webclient.orderByRoute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OrderByRouteWebClient {
    private final WebClient webClient;
    private final String orderByRouteUri;
    private final String routeUrlParamName;

    @Autowired
    public OrderByRouteWebClient(WebClient.Builder webClientBuilder,
                                 @Value("${cleanhub.base.url}") String baseUrl,
                                 @Value("${cleanhub.order-by-route.uri}") String orderByRouteUri,
                                 @Value("${cleanhub.order-by-route.route-url-param-name}") String routeUrlParamName
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.orderByRouteUri = orderByRouteUri;
        this.routeUrlParamName = routeUrlParamName;
    }

    public Mono<OrderByRoute> fetchOrdersByRoute(String landingPageRoute) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path(orderByRouteUri).queryParam(routeUrlParamName, landingPageRoute).build())
                .retrieve()
                .bodyToMono(OrderByRoute.class);
    }

}

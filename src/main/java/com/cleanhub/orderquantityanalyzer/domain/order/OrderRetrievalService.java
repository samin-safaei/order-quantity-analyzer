package com.cleanhub.orderquantityanalyzer.domain.order;

import com.cleanhub.orderquantityanalyzer.domain.customer.Customer;
import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.orderByRoute.OrderByRoute;
import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.orderByRoute.OrderByRouteRetrievalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderRetrievalService {

    private final OrderByRouteRetrievalService orderByRouteRetrievalService;
    private final OrderConverterService orderConverterService;

    public Order getOrderByCustomer(Customer customer) {
        OrderByRoute orderByRoute = orderByRouteRetrievalService.getOrder(customer.landingPageRoute());
        return orderConverterService.fromOrderByRouteAndCustomerId(orderByRoute, customer.id());
    }

}

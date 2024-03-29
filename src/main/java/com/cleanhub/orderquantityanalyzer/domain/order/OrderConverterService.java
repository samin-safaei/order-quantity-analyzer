package com.cleanhub.orderquantityanalyzer.domain.order;

import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.CustomerEntity;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.OrderEntity;
import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.orderByRoute.OrderByRoute;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderConverterService {

    public Order fromOrderByRouteAndCustomerId(OrderByRoute orderByRoute, UUID customerId) {
        return new Order(orderByRoute.uuid(), customerId, orderByRoute.companyName(), orderByRoute.quantity());
    }

    public Order fromOrderEntityAndCustomerId(OrderEntity orderEntity, UUID customerId) {
        return new Order(orderEntity.getId(), customerId, orderEntity.getCompanyName(), orderEntity.getQuantity());
    }

    public OrderEntity fromOrderAndCustomerEntity(Order order, CustomerEntity customerEntity) {
        return new OrderEntity()
                .setId(order.id())
                .setCustomer(customerEntity)
                .setQuantity(order.quantity())
                .setCompanyName(order.companyName());
    }

}

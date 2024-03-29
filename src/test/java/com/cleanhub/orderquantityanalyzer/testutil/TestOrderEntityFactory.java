package com.cleanhub.orderquantityanalyzer.testutil;

import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.CustomerEntity;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.OrderEntity;

import java.util.UUID;

public class TestOrderEntityFactory {

    public static OrderEntity fromCustomer(CustomerEntity customerEntity) {
        return new OrderEntity()
                .setId(UUID.randomUUID())
                .setCustomer(customerEntity)
                .setQuantity(10d)
                .setCompanyName(customerEntity.getCompanyName());
    }

}

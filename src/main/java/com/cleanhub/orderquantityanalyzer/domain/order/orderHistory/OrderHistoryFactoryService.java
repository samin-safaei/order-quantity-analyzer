package com.cleanhub.orderquantityanalyzer.domain.order.orderHistory;

import com.cleanhub.orderquantityanalyzer.domain.order.Order;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.OrderEntity;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.OrderHistoryEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderHistoryFactoryService {

    public OrderHistoryEntity fromUpdatedOrderEntity(OrderEntity oldOrderEntity, Order newOrder) {
        return new OrderHistoryEntity()
                .setOrderId(oldOrderEntity.getId())
                .setCustomerId(oldOrderEntity.getCustomer().getId())
                .setLatestQuantity(newOrder.quantity())
                .setQuantityChange(newOrder.quantity() - oldOrderEntity.getQuantity())
                .setCreatedAt(LocalDateTime.now());
    }

    public OrderHistoryEntity fromNewOrderEntity(OrderEntity newOrderEntity) {
        return new OrderHistoryEntity()
                .setOrderId(newOrderEntity.getId())
                .setCustomerId(newOrderEntity.getCustomer().getId())
                .setLatestQuantity(newOrderEntity.getQuantity())
                .setQuantityChange(newOrderEntity.getQuantity())
                .setCreatedAt(LocalDateTime.now());
    }

}

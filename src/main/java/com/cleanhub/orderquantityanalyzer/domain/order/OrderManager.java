package com.cleanhub.orderquantityanalyzer.domain.order;

import com.cleanhub.orderquantityanalyzer.domain.order.orderHistory.OrderHistoryManager;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.CustomerRepository;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.OrderRepository;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.CustomerEntity;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.OrderEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderManager {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderHistoryManager orderHistoryManager;
    private final OrderConverterService orderConverterService;

    @Transactional
    public void saveOrder(Order order) {
        Optional<OrderEntity> existingOrder = orderRepository.findById(order.id());

        if (existingOrder.isPresent()) {
            OrderEntity oldOrderEntity = existingOrder.get();
            orderHistoryManager.addOrderHistory(oldOrderEntity, order);
            oldOrderEntity.setQuantity(order.quantity());
            orderRepository.save(oldOrderEntity);
        } else {
            CustomerEntity customerEntity = customerRepository.getReferenceById(order.customerId());
            OrderEntity orderEntity = orderConverterService.fromOrderAndCustomerEntity(order, customerEntity);
            orderHistoryManager.newOrderHistory(orderEntity);
            orderRepository.save(orderEntity);
        }
    }

    public Order findByCustomerId(UUID customerId) {
        OrderEntity orderEntity = orderRepository.findByCustomer_Id(customerId).orElseThrow(IllegalArgumentException::new);
        return orderConverterService.fromOrderEntityAndCustomerId(orderEntity, customerId);
    }

}

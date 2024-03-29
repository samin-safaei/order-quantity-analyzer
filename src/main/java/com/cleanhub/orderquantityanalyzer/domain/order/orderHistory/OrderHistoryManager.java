package com.cleanhub.orderquantityanalyzer.domain.order.orderHistory;

import com.cleanhub.orderquantityanalyzer.domain.order.Order;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.OrderHistoryRepository;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.OrderEntity;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.OrderHistoryEntity;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.TopOrderHistorySummary;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderHistoryManager {

    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderHistoryFactoryService orderHistoryFactoryService;

    @Transactional
    public void addOrderHistory(OrderEntity oldOrderEntity, Order newOrder) {
        OrderHistoryEntity orderHistoryEntity =
                orderHistoryFactoryService.fromUpdatedOrderEntity(oldOrderEntity, newOrder);
        orderHistoryRepository.save(orderHistoryEntity);
    }

    @Transactional
    public void newOrderHistory(OrderEntity newOrderEntity) {
        OrderHistoryEntity orderHistoryEntity =
                orderHistoryFactoryService.fromNewOrderEntity(newOrderEntity);
        orderHistoryRepository.save(orderHistoryEntity);
    }

    public List<TopOrderHistorySummary> findTopOrderHistoryByChangeSum(TopOrderHistoryByChangeQueryParams param) {
        return orderHistoryRepository.findCustomerOrderSummaries(param.fromDateTime(), param.toDateTime(),
                PageRequest.of(param.pageNumber(), param.numberOfCompanies()));
    }

}

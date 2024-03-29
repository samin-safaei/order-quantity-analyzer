package com.cleanhub.orderquantityanalyzer.domain.order.orderHistory;

import com.cleanhub.orderquantityanalyzer.domain.customer.Customer;
import com.cleanhub.orderquantityanalyzer.domain.customer.CustomerManager;
import com.cleanhub.orderquantityanalyzer.domain.order.Order;
import com.cleanhub.orderquantityanalyzer.domain.order.OrderManager;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.TopOrderHistorySummary;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderHistoryDetailService {

    private final CustomerManager customerManager;
    private final OrderManager orderManager;
    private final OrderHistoryManager orderHistoryManager;

    public List<OrderHistoryDetail> getOrderHistoryDetails(TopOrderHistoryByChangeQueryParams param) {
        List<TopOrderHistorySummary> histories = orderHistoryManager.findTopOrderHistoryByChangeSum(param);
        return histories.stream().map(history -> {
            UUID customerId = history.customerId();
            Customer customer = customerManager.findById(customerId);
            Order order = orderManager.findByCustomerId(customerId);
            return new OrderHistoryDetail(customer.id(), order.id(), customer.companyName(),
                    customer.landingPageRoute(), order.quantity(), history.sumQuantityChange());
        }).toList();
    }

}

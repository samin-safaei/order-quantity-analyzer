package com.cleanhub.orderquantityanalyzer.application.orderHistory;

import com.cleanhub.orderquantityanalyzer.domain.order.orderHistory.OrderHistoryDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderHistoryDetailResponseConverter {

    public List<OrderHistoryDetailResponse> fromOrderHistoryDetails(List<OrderHistoryDetail> orderHistoryDetails) {
        return orderHistoryDetails.stream().map(this::fromOrderHistoryDetail).toList();
    }

    public OrderHistoryDetailResponse fromOrderHistoryDetail(OrderHistoryDetail orderHistoryDetail) {
        return new OrderHistoryDetailResponse(orderHistoryDetail.customerId(),
                orderHistoryDetail.orderId(),
                orderHistoryDetail.companyName(),
                orderHistoryDetail.landingPageRoute(),
                orderHistoryDetail.latestQuantity(),
                orderHistoryDetail.quantityChange());
    }

}

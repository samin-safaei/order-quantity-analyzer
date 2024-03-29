package com.cleanhub.orderquantityanalyzer.application.orderHistory;

import com.cleanhub.orderquantityanalyzer.domain.order.orderHistory.OrderHistoryDetail;
import com.cleanhub.orderquantityanalyzer.domain.order.orderHistory.OrderHistoryDetailService;
import com.cleanhub.orderquantityanalyzer.domain.order.orderHistory.TopOrderHistoryByChangeQueryParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderHistoryApplicationService {

    private final OrderHistoryDetailService orderHistoryDetailService;
    private final OrderHistoryDetailResponseConverter orderHistoryDetailResponseConverter;

    public List<OrderHistoryDetailResponse> getTopCustomersByQuantityChange(OrderHistoryDetailParams params) {
        TopOrderHistoryByChangeQueryParams convertedParams = new TopOrderHistoryByChangeQueryParams(
                params.pageNumber(),
                params.numberOfCustomers(),
                params.fromDateTime(),
                params.toDateTime());
        List<OrderHistoryDetail> historyDetails = orderHistoryDetailService.getOrderHistoryDetails(convertedParams);
        return orderHistoryDetailResponseConverter.fromOrderHistoryDetails(historyDetails);
    }

}

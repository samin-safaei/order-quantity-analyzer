package com.cleanhub.orderquantityanalyzer.presentation.controller;

import com.cleanhub.orderquantityanalyzer.application.orderHistory.OrderHistoryApplicationService;
import com.cleanhub.orderquantityanalyzer.application.orderHistory.OrderHistoryDetailParams;
import com.cleanhub.orderquantityanalyzer.application.orderHistory.OrderHistoryDetailResponse;
import com.cleanhub.orderquantityanalyzer.presentation.controller.request.TopCustomersByQuantityChangeRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orderHistory")
@AllArgsConstructor
public class OrderHistoryController {

    private final OrderHistoryApplicationService orderHistoryApplicationService;

    @PostMapping("/topCustomersByQuantityChange")
    public ResponseEntity<List<OrderHistoryDetailResponse>> getTopCustomersByQuantityChange(@RequestBody TopCustomersByQuantityChangeRequest request) {
        OrderHistoryDetailParams requestParams = new OrderHistoryDetailParams(request.pageNumber(),
                request.numberOfCompanies(), request.fromDateTime(), request.toDateTime());
        List<OrderHistoryDetailResponse> result = orderHistoryApplicationService.getTopCustomersByQuantityChange(requestParams);
        return ResponseEntity.ok(result);
    }

}

package com.cleanhub.orderquantityanalyzer.testutil;

import com.cleanhub.orderquantityanalyzer.application.orderHistory.OrderHistoryDetailResponse;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.OrderHistoryEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class TestOrderHistoryFactory {

    public static OrderHistoryEntity from(UUID customerId, Double latestQuantity, Double quantityChange) {
        return from(customerId, latestQuantity, quantityChange, LocalDateTime.now());
    }

    public static OrderHistoryEntity from(UUID customerId, Double latestQuantity, Double quantityChange, LocalDateTime dateTime) {
        return new OrderHistoryEntity(null, UUID.randomUUID(), customerId, latestQuantity, quantityChange, dateTime);
    }

    public static List<OrderHistoryDetailResponse> getOrderHistoryDetailResponses() {
        return getOrderHistoryDetailResponses(10);
    }

    public static List<OrderHistoryDetailResponse> getOrderHistoryDetailResponses(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> get())
                .toList();
    }

    public static OrderHistoryDetailResponse get() {
        return new OrderHistoryDetailResponse(UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                10d, 10d);
    }

}

package com.cleanhub.orderquantityanalyzer.testutil;

import com.cleanhub.orderquantityanalyzer.domain.order.Order;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class TestOrderFactory {

    public static List<Order> getOrders(int count) {
        return IntStream.of(0, count)
                .mapToObj(i -> get())
                .toList();
    }

    public static Order get() {
        return new Order(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID().toString(), 10d);
    }

}

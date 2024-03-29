package com.cleanhub.orderquantityanalyzer.infrastructure.repository;

import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.OrderHistoryEntity;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.TopOrderHistorySummary;
import com.cleanhub.orderquantityanalyzer.testutil.AbstractIntegrationTest;
import com.cleanhub.orderquantityanalyzer.testutil.DateUtil;
import com.cleanhub.orderquantityanalyzer.testutil.TestOrderHistoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
public class OrderHistoryRepositoryTest extends AbstractIntegrationTest {

    private final OrderHistoryRepository underTest;

    @Autowired
    public OrderHistoryRepositoryTest(OrderHistoryRepository underTest) {
        this.underTest = underTest;
    }

    @BeforeEach
    public void setUp() {
        underTest.deleteAll();
    }

    @Test
    public void findCustomerOrderSummaries() {
        // WHEN
        UUID customerId1 = UUID.randomUUID();
        UUID customerId2 = UUID.randomUUID();
        UUID customerId3 = UUID.randomUUID();
        OrderHistoryEntity orderHistory1 = TestOrderHistoryFactory.from(customerId1, 10d, 9d);
        OrderHistoryEntity orderHistory2 = TestOrderHistoryFactory.from(customerId2, 9d, 9d);
        OrderHistoryEntity orderHistory3 = TestOrderHistoryFactory.from(customerId3, 8d, 9d);
        OrderHistoryEntity orderHistory4 = TestOrderHistoryFactory.from(customerId1, 12d, 2d);
        OrderHistoryEntity orderHistory5 = TestOrderHistoryFactory.from(customerId2, 10d, 1d);

        // GIVEN
        underTest.saveAll(Arrays.asList(orderHistory1, orderHistory2, orderHistory3, orderHistory4, orderHistory5));
        List<TopOrderHistorySummary> actualResult = underTest.findCustomerOrderSummaries(
                DateUtil.yesterday(),
                LocalDateTime.now(),
                PageRequest.of(0, 10));

        // THEN
        assertNotNull(actualResult);
        assertEquals(3, actualResult.size());
        assertEquals(customerId1, actualResult.get(0).customerId());
        assertEquals(customerId2, actualResult.get(1).customerId());
        assertEquals(customerId3, actualResult.get(2).customerId());
        assertEquals(11d, actualResult.get(0).sumQuantityChange());
        assertEquals(10d, actualResult.get(1).sumQuantityChange());
    }

    @Test
    public void findCustomerOrderSummaries_testDate() {
        // WHEN
        UUID customerId1 = UUID.randomUUID();
        UUID customerId2 = UUID.randomUUID();
        UUID customerId3 = UUID.randomUUID();
        OrderHistoryEntity orderHistory1 = TestOrderHistoryFactory.from(customerId1, 10d, 9d, DateUtil.yesterday());
        OrderHistoryEntity orderHistory2 = TestOrderHistoryFactory.from(customerId2, 9d, 8d, DateUtil.yesterday());
        OrderHistoryEntity orderHistory3 = TestOrderHistoryFactory.from(customerId3, 8d, 7d, DateUtil.yesterday());
        OrderHistoryEntity orderHistory4 = TestOrderHistoryFactory.from(customerId1, 12d, 2d, DateUtil.lastMonth());
        OrderHistoryEntity orderHistory5 = TestOrderHistoryFactory.from(customerId2, 10d, 1d, DateUtil.lastMonth());

        // GIVEN
        underTest.saveAll(Arrays.asList(orderHistory1, orderHistory2, orderHistory3, orderHistory4, orderHistory5));
        List<TopOrderHistorySummary> actualResult = underTest.findCustomerOrderSummaries(
                DateUtil.minusDays(2),
                LocalDateTime.now(),
                PageRequest.of(0, 10));

        // THEN
        assertNotNull(actualResult);
        assertEquals(3, actualResult.size());
        assertEquals(customerId1, actualResult.get(0).customerId());
        assertEquals(customerId2, actualResult.get(1).customerId());
        assertEquals(customerId3, actualResult.get(2).customerId());
        assertEquals(9d, actualResult.get(0).sumQuantityChange());
        assertEquals(8d, actualResult.get(1).sumQuantityChange());
    }
}
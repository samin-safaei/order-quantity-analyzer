package com.cleanhub.orderquantityanalyzer.infrastructure.repository;

import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.OrderHistoryEntity;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.TopOrderHistorySummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistoryEntity, BigInteger> {

    @Query(value = "SELECT new com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.TopOrderHistorySummary(o.customerId, SUM(o.quantityChange)) " +
            "FROM OrderHistoryEntity o " +
            "WHERE o.createdAt BETWEEN :fromDate AND :toDate " +
            "GROUP BY o.customerId " +
            "ORDER BY SUM(o.quantityChange) DESC")
    List<TopOrderHistorySummary> findCustomerOrderSummaries(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);

}

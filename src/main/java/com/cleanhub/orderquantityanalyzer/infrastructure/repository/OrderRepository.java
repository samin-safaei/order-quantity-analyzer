package com.cleanhub.orderquantityanalyzer.infrastructure.repository;

import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    Optional<OrderEntity> findByCustomer_Id(UUID customerId);

}

package com.cleanhub.orderquantityanalyzer.infrastructure.repository;

import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {
}

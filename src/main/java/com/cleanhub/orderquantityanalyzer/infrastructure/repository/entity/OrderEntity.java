package com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Table(name = "order_quantity")
@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = "customer")
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

    @Id
    private UUID id;

    private String companyName;

    private Double quantity;

    @JoinColumn(name = "customer_id")
    @ManyToOne(targetEntity = CustomerEntity.class, fetch = FetchType.LAZY)
    private CustomerEntity customer;

}

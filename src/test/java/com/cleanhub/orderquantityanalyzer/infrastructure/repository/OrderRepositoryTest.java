package com.cleanhub.orderquantityanalyzer.infrastructure.repository;

import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.CustomerEntity;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.OrderEntity;
import com.cleanhub.orderquantityanalyzer.testutil.AbstractIntegrationTest;
import com.cleanhub.orderquantityanalyzer.testutil.TestCustomerEntityFactory;
import com.cleanhub.orderquantityanalyzer.testutil.TestOrderEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
public class OrderRepositoryTest extends AbstractIntegrationTest {

    private final OrderRepository underTest;
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderRepositoryTest(OrderRepository underTest, CustomerRepository customerRepository) {
        this.underTest = underTest;
        this.customerRepository = customerRepository;
    }

    @BeforeEach
    public void setUp() {
        underTest.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void save() {
        // GIVEN
        CustomerEntity customerEntity = TestCustomerEntityFactory.getCustomerEntity();
        customerEntity = customerRepository.save(customerEntity);

        // WHEN
        OrderEntity orderEntity = TestOrderEntityFactory.fromCustomer(customerEntity);

        // THEN
        assertDoesNotThrow(() -> underTest.save(orderEntity));
    }

    @Test
    public void saveAndGet() {
        // GIVEN
        CustomerEntity customerEntity = TestCustomerEntityFactory.getCustomerEntity();
        customerEntity = customerRepository.save(customerEntity);

        // WHEN
        OrderEntity orderEntity = TestOrderEntityFactory.fromCustomer(customerEntity);
        OrderEntity savedOrderEntity = underTest.save(orderEntity);
        OrderEntity foundOrderEntity = underTest.findById(savedOrderEntity.getId()).orElse(null);

        // THEN
        assertNotNull(foundOrderEntity);
        assertEquals(savedOrderEntity, foundOrderEntity);
    }

    @Test
    public void findByCustomer_Id() {
        // GIVEN
        CustomerEntity customerEntity = TestCustomerEntityFactory.getCustomerEntity();
        customerEntity = customerRepository.save(customerEntity);
        OrderEntity orderEntity = TestOrderEntityFactory.fromCustomer(customerEntity);
        OrderEntity savedOrderEntity = underTest.save(orderEntity);

        // WHEN
        OrderEntity foundOrderEntity = underTest.findByCustomer_Id(customerEntity.getId()).orElse(null);

        // THEN
        assertNotNull(foundOrderEntity);
        assertEquals(savedOrderEntity, foundOrderEntity);
    }
}
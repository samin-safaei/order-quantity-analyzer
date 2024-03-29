package com.cleanhub.orderquantityanalyzer.infrastructure.repository;

import com.cleanhub.orderquantityanalyzer.testutil.AbstractIntegrationTest;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.CustomerEntity;
import com.cleanhub.orderquantityanalyzer.testutil.TestCustomerEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
public class CustomerRepositoryTest extends AbstractIntegrationTest {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRepositoryTest(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @BeforeEach
    public void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    public void saveAndGet() {
        // GIVEN
        CustomerEntity expectedEntity = TestCustomerEntityFactory.getCustomerEntity();
        CustomerEntity savedEntity = customerRepository.save(expectedEntity);

        // WHEN
        CustomerEntity actualCustomerEntity = customerRepository.findById(savedEntity.getId()).orElse(null);

        // THEN
        assertNotNull(actualCustomerEntity);
        assertEquals(expectedEntity.getCompanyName(), actualCustomerEntity.getCompanyName());
        assertEquals(expectedEntity.getLandingPageRoute(), actualCustomerEntity.getLandingPageRoute());
    }

    @Test
    public void saveAndGetMultiple() {
        // GIVEN
        List<CustomerEntity> expectedEntities = TestCustomerEntityFactory.getCustomerEntities();
        customerRepository.saveAll(expectedEntities);

        // WHEN
        List<CustomerEntity> actualCustomerEntities = customerRepository.findAll();

        // THEN
        assertNotNull(actualCustomerEntities);
        assertEquals(expectedEntities.size(), actualCustomerEntities.size());
    }

    @Test
    public void delete() {
        // GIVEN
        CustomerEntity expectedEntity = TestCustomerEntityFactory.getCustomerEntity();
        CustomerEntity savedEntity = customerRepository.save(expectedEntity);

        // WHEN
        customerRepository.delete(savedEntity);

        // THEN
        CustomerEntity actualDeletedEntity = customerRepository.findById(savedEntity.getId()).orElse(null);
        assertNull(actualDeletedEntity);
    }
}
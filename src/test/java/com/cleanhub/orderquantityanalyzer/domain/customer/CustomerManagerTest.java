package com.cleanhub.orderquantityanalyzer.domain.customer;

import com.cleanhub.orderquantityanalyzer.infrastructure.repository.CustomerRepository;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.CustomerEntity;
import com.cleanhub.orderquantityanalyzer.testutil.TestCustomerEntityFactory;
import com.cleanhub.orderquantityanalyzer.testutil.TestCustomerFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class CustomerManagerTest {

    private CustomerManager underTest;
    private CustomerRepository mockCustomerRepository;
    private CustomerConverterService mockCustomerConverterService;

    @BeforeEach
    void setup() {
        mockCustomerRepository = Mockito.mock();
        mockCustomerConverterService = Mockito.mock();
        this.underTest = new CustomerManager(mockCustomerRepository, mockCustomerConverterService);
    }

    @Test
    void saveNewCustomers_savesNewCustomersAsExpected() {
        // GIVEN
        List<Customer> newCustomers = TestCustomerFactory.getCustomersWithoutId();
        List<CustomerEntity> newCustomerEntities = TestCustomerEntityFactory.fromCustomers(newCustomers);
        List<CustomerEntity> expectedCustomerEntities = TestCustomerEntityFactory.getCustomerEntities();

        // WHEN
        when(mockCustomerRepository.findAll()).thenReturn(Collections.emptyList());
        when(mockCustomerConverterService.fromCustomers(eq(newCustomers))).thenReturn(newCustomerEntities);
        when(mockCustomerRepository.saveAll(newCustomerEntities)).thenReturn(expectedCustomerEntities);
        underTest.saveNewCustomers(newCustomers);

        //THEN
        verify(mockCustomerRepository, times(1)).saveAll(eq(newCustomerEntities));
    }

    @Test
    void saveNewCustomers_shouldExcludeExistingCustomers() {
        // GIVEN
        List<Customer> newCustomers = TestCustomerFactory.getCustomersWithoutId();
        List<Customer> existingCustomers = TestCustomerFactory.getCustomersWithoutId();
        List<CustomerEntity> existingCustomerEntities = TestCustomerEntityFactory.fromCustomersWithId(existingCustomers);
        List<Customer> allCustomers = new ArrayList<>();
        allCustomers.addAll(newCustomers);
        allCustomers.addAll(existingCustomers);

        List<CustomerEntity> newCustomerEntities = TestCustomerEntityFactory.fromCustomers(newCustomers);
        List<CustomerEntity> expectedCustomerEntities = TestCustomerEntityFactory.getCustomerEntities();

        // WHEN
        when(mockCustomerRepository.findAll()).thenReturn(existingCustomerEntities);
        when(mockCustomerConverterService.fromCustomers(eq(newCustomers))).thenReturn(newCustomerEntities);
        when(mockCustomerRepository.saveAll(newCustomerEntities)).thenReturn(expectedCustomerEntities);
        underTest.saveNewCustomers(allCustomers);

        //THEN
        verify(mockCustomerRepository, times(1)).saveAll(eq(newCustomerEntities));
    }

    @Test
    void saveNewCustomers_shouldNotSaveAnyCustomers() {
        // GIVEN
        List<Customer> existingCustomers = TestCustomerFactory.getCustomersWithoutId();
        List<CustomerEntity> existingCustomerEntities = TestCustomerEntityFactory.fromCustomersWithId(existingCustomers);

        // WHEN
        when(mockCustomerRepository.findAll()).thenReturn(existingCustomerEntities);
        underTest.saveNewCustomers(existingCustomers);

        //THEN
        verify(mockCustomerRepository, Mockito.times(1)).findAll();
        verify(mockCustomerConverterService, never()).fromCustomers(anyList());
        verify(mockCustomerRepository, never()).saveAll(anyList());
    }

    @Test
    void findAll() {
        // GIVEN
        List<CustomerEntity> expectedCustomerEntities = TestCustomerEntityFactory.getCustomerEntities();
        List<Customer> expectedCustomers = expectedCustomerEntities.stream()
                .map(customerEntity -> TestCustomerFactory.get(customerEntity.getId(),
                        customerEntity.getCompanyName(),
                        customerEntity.getLandingPageRoute())
                ).toList();

        // WHEN
        when(mockCustomerRepository.findAll()).thenReturn(expectedCustomerEntities);
        when(mockCustomerConverterService.fromCustomerEntities(eq(expectedCustomerEntities))).thenReturn(expectedCustomers);
        List<Customer> actualCustomers = underTest.findAll();

        //THEN
        assertNotNull(actualCustomers);
        assertEquals(expectedCustomers.size(), actualCustomers.size());
        assertEquals(expectedCustomers, actualCustomers);
    }
}
package com.cleanhub.orderquantityanalyzer.domain.customer;

import com.cleanhub.orderquantityanalyzer.infrastructure.repository.CustomerRepository;
import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.CustomerEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerManager {

    private final CustomerRepository customerRepository;
    private final CustomerConverterService customerConverterService;

    /**
     * @param customers all {@code customers} as input
     * @return list of newly saved {@code CustomerEntity} or an empty list
     */
    @Transactional
    public void saveNewCustomers(List<Customer> customers) {
        logger.info("Saving new customers count:{}", customers.size());
        List<CustomerEntity> existingCustomers = customerRepository.findAll();
        logger.info("Number of existing customers:{}", existingCustomers.size());
        Set<String> existingLandingPageRoutes = existingCustomers.stream().map(CustomerEntity::getLandingPageRoute)
                .collect(Collectors.toSet());
        List<Customer> newCustomers = customers.stream()
                .filter(customer -> !existingLandingPageRoutes.contains(customer.landingPageRoute()))
                .toList();

        List<CustomerEntity> newCustomerEntities = Collections.emptyList();
        if (newCustomers.size() > 0) {
            newCustomerEntities = customerConverterService.fromCustomers(newCustomers);
        }

        if (newCustomerEntities.size() > 0)
            customerRepository.saveAll(newCustomerEntities);
    }

    public List<Customer> findAll() {
        return customerConverterService.fromCustomerEntities(customerRepository.findAll());
    }

    public Customer findById(UUID customerId) {
        CustomerEntity customerEntity = customerRepository.findById(customerId)
                .orElseThrow(IllegalArgumentException::new);
        return customerConverterService.fromCustomerEntity(customerEntity);
    }

}

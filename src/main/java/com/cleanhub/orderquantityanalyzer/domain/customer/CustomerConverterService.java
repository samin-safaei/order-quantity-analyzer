package com.cleanhub.orderquantityanalyzer.domain.customer;

import com.cleanhub.orderquantityanalyzer.infrastructure.repository.entity.CustomerEntity;
import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.customerLogo.CustomerLogo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerConverterService {

    public List<Customer> fromCustomerLogos(List<CustomerLogo> customerLogos) {
        return customerLogos.stream()
                .map(this::fromCustomerLogo)
                .toList();
    }

    public Customer fromCustomerLogo(CustomerLogo customerLogo) {
        return new Customer(null, customerLogo.companyName(), customerLogo.landingPageRoute());
    }

    public List<Customer> fromCustomerEntities(List<CustomerEntity> customerEntities) {
        return customerEntities.stream().map(this::fromCustomerEntity).toList();
    }

    public Customer fromCustomerEntity(CustomerEntity customerEntity) {
        return new Customer(customerEntity.getId(), customerEntity.getCompanyName(), customerEntity.getLandingPageRoute());
    }

    public List<CustomerEntity> fromCustomers(List<Customer> customers) {
        return customers.stream().map(this::fromCustomer).toList();
    }

    public CustomerEntity fromCustomer(Customer customer) {
        return new CustomerEntity(customer.id(), customer.companyName(), customer.landingPageRoute());
    }

}

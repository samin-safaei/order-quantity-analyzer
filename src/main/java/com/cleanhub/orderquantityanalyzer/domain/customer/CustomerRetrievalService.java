package com.cleanhub.orderquantityanalyzer.domain.customer;

import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.customerLogo.CustomerLogo;
import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.customerLogo.CustomerLogoRetrievalService;
import com.cleanhub.orderquantityanalyzer.infrastructure.webclient.exceptions.WebClientRetrievalException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerRetrievalService {

    private final CustomerLogoRetrievalService customerLogoRetrievalService;
    private final CustomerConverterService customerConverterService;

    public List<Customer> getAll() throws WebClientRetrievalException {
        List<CustomerLogo> fetchedCustomerLogos = customerLogoRetrievalService.getCustomers();
        return customerConverterService.fromCustomerLogos(fetchedCustomerLogos);
    }

}

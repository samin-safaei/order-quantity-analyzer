package com.cleanhub.orderquantityanalyzer.testutil;

import com.cleanhub.orderquantityanalyzer.domain.customer.Customer;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class TestCustomerFactory {

    /**
     * @return List of 10 random customers generated by {@link #getCustomerWithoutId()}
     */
    public static List<Customer> getCustomersWithoutId() {
        return getCustomersWithoutId(10);
    }

    /**
     * @param count of customers to be returned
     * @return List of random customers using {@code count}, generated by {@link #getCustomerWithoutId()}
     */
    public static List<Customer> getCustomersWithoutId(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> getCustomerWithoutId())
                .toList();
    }

    /**
     * @return A customer using random UUIDs for strings delegating to {@link #getCustomerWithoutId(String, String)}
     */
    public static Customer getCustomerWithoutId() {
        return getCustomerWithoutId(UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    /**
     * @return A customer from passed parameters
     */
    public static Customer getCustomerWithoutId(String companyName, String landingPageRoute) {
        return new Customer(null, companyName, landingPageRoute);
    }

    /**
     * @return 10 customers with random fields
     */
    public static List<Customer> getCustomers() {
        return getCustomers(10);
    }

    /**
     * @return 10 customers with random fields
     */
    public static List<Customer> getCustomers(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> get())
                .toList();
    }

    /**
     * @return A customer with all random params
     */
    public static Customer get() {
        return new Customer(UUID.randomUUID(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    /**
     * @return A customer from passed parameters
     */
    public static Customer get(UUID id, String companyName, String landingPageRoute) {
        return new Customer(id, companyName, landingPageRoute);
    }

}

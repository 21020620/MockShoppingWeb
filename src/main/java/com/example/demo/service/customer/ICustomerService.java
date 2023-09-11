package com.example.demo.service.customer;

import com.example.demo.entities.Customer;

public interface ICustomerService {
    void addCustomer(Customer customer);
    void deleteCustomer(Long id);
    String getAllCustomers();
    Customer getCustomerById(Long id);
}

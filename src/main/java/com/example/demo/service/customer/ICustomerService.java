package com.example.demo.service.customer;

import com.example.demo.entities.Customer;
import com.example.demo.entities.Product;

public interface ICustomerService {
    void addCustomer(Customer customer);
    void deleteCustomer(Long id);
    String getAllCustomers();
    Customer getCustomerById(Long id);
    Customer getCustomerByEmail(String email);

    boolean addProductToCart(Customer customer, Product product, int quantity);
    String checkOut(Customer customer);
    void updateCustomer(Customer c1, Customer c2);
}

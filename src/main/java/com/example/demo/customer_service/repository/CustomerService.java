package com.example.demo.customer_service.repository;

import com.example.demo.customer_service.entity.Customer;
import com.example.demo.product_service.entity.Product;

public interface CustomerService {
    void addCustomer(Customer customer);
    void deleteCustomer(Long id);
    String getAllCustomers();
    Customer getCustomerById(Long id);
    Customer getCustomerByEmail(String email);

    void addProductToCart(Customer customer, Product product, int quantity);
    String checkOut(Customer customer);
    void updateCustomer(Customer c1, Customer c2);
    String showCart(Customer customer);
}

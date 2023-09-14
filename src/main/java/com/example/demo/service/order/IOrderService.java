package com.example.demo.service.order;

import com.example.demo.entities.Customer;
import com.example.demo.entities.Order;
import com.example.demo.entities.Product;

public interface IOrderService {
    void addOrder(Order order);
    void deleteOrder(Long id);
    Order findOrderByCustomerAndProduct(Customer customer, Product product);

}

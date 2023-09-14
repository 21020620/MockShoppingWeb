package com.example.demo.service.order;

import com.example.demo.entities.Customer;
import com.example.demo.entities.Order;
import com.example.demo.entities.Product;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;

    public void addOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        boolean exists = orderRepository.existsById(id);
        if (!exists) {
            System.out.println("Order with id " + id + " does not exist");
            return;
        }
        orderRepository.deleteById(id);
        System.out.println("Order removed: " + id);
    }

    @Override
    public Order findOrderByCustomerAndProduct(Customer customer, Product product) {
        return orderRepository.findByCustomerAndProduct(customer, product);
    }
}

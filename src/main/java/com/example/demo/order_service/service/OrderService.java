package com.example.demo.order_service.service;

import com.example.demo.system_service.exception.CustomerException;
import com.example.demo.order_service.entity.Order;
import com.example.demo.order_service.repository.IOrderService;
import com.example.demo.order_service.repository.OrderRepository;
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
        if (!exists)
            throw new CustomerException("Order with id " + id + " does not exist");
        orderRepository.deleteById(id);
    }
}

package com.example.demo.service.order;

import com.example.demo.entities.CustomerException;
import com.example.demo.entities.Order;
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
        if (!exists)
            throw new CustomerException("Order with id " + id + " does not exist");
        orderRepository.deleteById(id);
    }
}

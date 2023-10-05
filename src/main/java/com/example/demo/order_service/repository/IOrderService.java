package com.example.demo.order_service.repository;

import com.example.demo.order_service.entity.Order;

public interface IOrderService {
    void addOrder(Order order);
    void deleteOrder(Long id);


}

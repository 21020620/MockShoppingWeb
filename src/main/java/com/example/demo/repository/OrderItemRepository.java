package com.example.demo.repository;

import com.example.demo.entities.Customer;
import com.example.demo.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT DISTINCT oi.product.id FROM OrderItem oi WHERE oi.customer = :customer")
    List<Long> findDistinctProductIdsByCustomer(Customer customer);
}

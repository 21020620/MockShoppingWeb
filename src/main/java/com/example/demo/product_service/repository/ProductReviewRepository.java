package com.example.demo.product_service.repository;

import com.example.demo.customer_service.entity.Customer;
import com.example.demo.product_service.entity.Product;
import com.example.demo.product_service.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    @Query("SELECT DISTINCT pr.reviewer FROM ProductReview pr WHERE pr.product = :product")
    List<Customer> findDistinctCustomersByProduct(Product product);
}

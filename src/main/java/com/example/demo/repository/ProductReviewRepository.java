package com.example.demo.repository;

import com.example.demo.entities.Customer;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    @Query("SELECT DISTINCT pr.reviewer FROM ProductReview pr WHERE pr.product = :product")
    List<Customer> findDistinctCustomersByProduct(Product product);
}

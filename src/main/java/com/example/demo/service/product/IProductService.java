package com.example.demo.service.product;

import com.example.demo.entities.Customer;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductReview;

import java.util.Optional;

public interface IProductService {
    void addProduct(Product product);
    void deleteProduct(Long id);
    String getAllProducts();
    Product getProductById(Long id);
    void updateProduct(Product p1, Product p2);
    void addReview(Long id, ProductReview productReview, Customer reviewer);
    String showAllReviews(Long id);
}

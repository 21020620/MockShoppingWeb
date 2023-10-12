package com.example.demo.product_service.repository;

import com.example.demo.customer_service.entity.Customer;
import com.example.demo.product_service.entity.Product;
import com.example.demo.product_service.entity.ProductReview;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    void addProduct(Product product);
    void deleteProduct(Long id);
    String getAllProducts();
    Product getProductById(Long id);
    void updateProduct(Product p1, Product p2);
    void addReview(Long id, ProductReview productReview, Customer reviewer);
    String showAllReviews(Long id);
    void addImageToProduct(Long id, MultipartFile imageFile) throws IOException;
    Resource getImage(Long id);
}

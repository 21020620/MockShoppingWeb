package com.example.demo.service.product;

import com.example.demo.entities.Product;

public interface IProductService {
    void addProduct(Product product);
    void deleteProduct(Long id);
    String getAllProducts();
    Product getProductById(Long id);
    void updateProduct(Product p1, Product p2);
}

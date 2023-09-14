package com.example.demo.service.product;

import com.example.demo.entities.Product;
import com.example.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    public void deleteProduct(Long id) {
        boolean exists = productRepository.existsById(id);
        if (!exists) {
            System.out.println("Product with id " + id + " does not exist");
        }
        productRepository.deleteById(id);
        System.out.println("Product removed: " + id);
    }

    @Transactional
    public String getAllProducts() {
        StringBuilder sb = new StringBuilder();
        productRepository.findAll().forEach(product -> sb.append(product.toString()).append("\n"));
        return sb.toString();
    }

    @Transactional
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional
    public void updateProduct(Product p1, Product p2) {
        p1.setName(p2.getName() != null ? p2.getName() : p1.getName());
        p1.setPrice(p2.getPrice() > 0 ? p2.getPrice() : p1.getPrice());
        p1.setQuantityInStock(p2.getQuantityInStock() >= 0 ? p2.getQuantityInStock() : p1.getQuantityInStock());
        productRepository.save(p1);
    }
}

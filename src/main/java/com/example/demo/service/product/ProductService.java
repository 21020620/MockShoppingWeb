package com.example.demo.service.product;

import com.example.demo.entities.Product;
import com.example.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {
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
}

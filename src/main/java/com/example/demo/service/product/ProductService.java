package com.example.demo.service.product;

import com.example.demo.entities.Customer;
import com.example.demo.entities.CustomerException;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductReview;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductReviewRepository productReviewRepository;

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
        return productRepository.findById(id).orElseThrow(() -> new CustomerException("Product not found"));
    }

    @Transactional
    public void updateProduct(Product p1, Product p2) {
        p1.setName(p2.getName() != null ? p2.getName() : p1.getName());
        p1.setPrice(p2.getPrice() > 0 ? p2.getPrice() : p1.getPrice());
        p1.setQuantityInStock(p2.getQuantityInStock() >= 0 ? p2.getQuantityInStock() : p1.getQuantityInStock());
        productRepository.save(p1);
    }

    @Transactional
    public void addReview(Long productID, ProductReview review, Customer reviewer) {
        Product product = productRepository.findById(productID).orElseThrow(() -> new CustomerException("Product not found"));
        List<Long> productIDs = orderItemRepository.findDistinctProductIdsByCustomer(reviewer);
        if (!productIDs.contains(productID))
            throw new CustomerException("You cannot review a product you have not purchased");
        List<Customer> reviewers = productReviewRepository.findDistinctCustomersByProduct(product);
        if (reviewers.contains(reviewer))
            throw new CustomerException("You cannot review a product more than once");
        review.setReviewer(reviewer);
        review.setProduct(product);
        product.setRating((product.getRating() * product.getReviews().size() + review.getRating()) / (product.getReviews().size() + 1));
        product.getReviews().add(review);
    }

    @Override
    public String showAllReviews(Long productID) {
        Product product = productRepository.findById(productID).orElseThrow(() -> new CustomerException("Product not found"));
        StringBuilder sb = new StringBuilder();
        product.getReviews().forEach(review -> sb.append(review.toString()).append("\n"));
        return sb.toString();
    }
}

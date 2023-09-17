package com.example.demo.controller;

import com.example.demo.entities.*;
import com.example.demo.service.customer.ICustomerService;
import com.example.demo.service.product.IProductService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
public class PostController {
    @Autowired
    private IProductService productService;
    @Autowired
    private ICustomerService customerService;

    private static final Logger logger = ApplicationLogger.getLogger();
    @PostMapping("api/post/products")
    public ResponseEntity<?> createProduct(@RequestPart("imageFile") MultipartFile imageFile,
                                           @RequestPart("productData") Product product) {
        try {
            byte[] imageBytes = imageFile.getBytes();
            product.setImage(imageBytes);
            System.out.println(product);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().body("Error when uploading image");
        }
        productService.addProduct(product);
        logger.info("Product created: " + product);
        return ResponseEntity.ok("File received successful\nProduct created");
    }

    @PostMapping("service/addToCart")
    public ResponseEntity<?> addProductToCartI(@RequestParam Long id, @RequestParam int quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Customer customer = customerService.getCustomerByEmail(userDetails.getUsername());
        Product product = productService.getProductById(id);
        customerService.addProductToCart(customer, product, quantity);
        logger.info("Product added to cart");
        return ResponseEntity.ok("Product added to cart");
    }

    @PostMapping(value = {"api/post/customer", "register"})
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        customer.setAccount(new Account(customer.getEmail(), customer.getPassword(), "ROLE_Customer"));
        customerService.addCustomer(customer);
        System.out.println("Password: " + customer.getPassword());
        logger.info("Customer created");
        return ResponseEntity.status(201).body("Customer created");
    }

    @PostMapping("service/checkOut")
    public ResponseEntity<?> customerCheckOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Customer customer = customerService.getCustomerByEmail(userDetails.getUsername());
        logger.info("Customer checked out");
        return ResponseEntity.ok(customerService.checkOut(customer));
    }

    @PostMapping("service/addReview/{productId}")
    public ResponseEntity<?> addReview(@PathVariable Long productId, @RequestBody ProductReview productReview) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Customer customer = customerService.getCustomerByEmail(userDetails.getUsername());
        productService.addReview(productId, productReview, customer);
        return ResponseEntity.ok("Review added");
    }
}

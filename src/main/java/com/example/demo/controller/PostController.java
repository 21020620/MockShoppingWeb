package com.example.demo.controller;

import com.example.demo.entities.*;
import com.example.demo.service.account.IAccountService;
import com.example.demo.service.customer.ICustomerService;
import com.example.demo.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
public class PostController {
    @Autowired
    private IProductService productService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private ICustomerService customerService;

    private static final Logger logger = ApplicationLogger.getLogger();
    @PostMapping("api/post/products")
    public void createProduct(@RequestBody Product product) {
        logger.info("Product created: " + product.toString());
        productService.addProduct(product);
    }

    @PostMapping("service/addToCart")
    public ResponseEntity<?> addProductToCartI(@RequestParam Long id, @RequestParam int quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Customer customer = customerService.getCustomerByEmail(userDetails.getUsername());
        Product product = productService.getProductById(id);
        if(product == null) {
            logger.info("Product not found");
            return ResponseEntity.badRequest().body("Product not found");
        }
        if (!customerService.addProductToCart(customer, product, quantity)){
            logger.info("Not enough product in stock");
            return ResponseEntity.badRequest().body("Not enough product in stock");
        }
        logger.info("Product added to cart");
        return ResponseEntity.ok("Product added to cart");
    }

    @PostMapping("api/post/customer")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        Account account = new Account(customer.getEmail(), customer.getPassword(), "ROLE_Customer");
        customer.setAccount(account);
        customerService.addCustomer(customer);
        logger.info("Customer created");
        return ResponseEntity.status(201).body("Customer created");
    }

    @PostMapping("service/checkOut")
    public ResponseEntity<?> customerCheckOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Customer customer = customerService.getCustomerByEmail(userDetails.getUsername());
        return ResponseEntity.ok(customerService.checkOut(customer));
    }
}

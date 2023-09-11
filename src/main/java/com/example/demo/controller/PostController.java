package com.example.demo.controller;

import com.example.demo.entities.*;
import com.example.demo.service.account.IAccountService;
import com.example.demo.service.customer.ICustomerService;
import com.example.demo.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void addProductToCart(@RequestBody Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Customer customer = customerService.getCustomerByEmail(userDetails.getUsername());

    }
}

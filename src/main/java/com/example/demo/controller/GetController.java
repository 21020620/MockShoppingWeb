package com.example.demo.controller;

import com.example.demo.entities.Customer;
import com.example.demo.entities.Product;
import com.example.demo.service.account.IAccountService;
import com.example.demo.service.customer.ICustomerService;
import com.example.demo.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping()
public class GetController {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IProductService productService;
    @Autowired
    private ICustomerService customerService;

    @GetMapping("api/get/accounts")
    public String getAllAccount() {
        return accountService.getAllAccounts();
    }

    @GetMapping(value = {"api/get/products", "service/products"})
    public String getAllProduct() {
        return productService.getAllProducts();
    }

    @GetMapping("api/get/customers")
    public String getAllCustomer() {
        return customerService.getAllCustomers();
    }

    @GetMapping("api/get/customer/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("service/showCart")
    public ResponseEntity<?> showCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Customer customer = customerService.getCustomerByEmail(userDetails.getUsername());
        return ResponseEntity.ok(customerService.showCart(customer));
    }

    @GetMapping(value = {"api/getProductImage/{productId}", "service/getProductImage/{productId}"})
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        if (product == null || product.getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok()
                .headers(headers)
                .body(product.getImage());
    }

    @GetMapping("api/getProduct/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping(value = {"api/getProductReviews/{productId}", "service/getProductReviews/{productId}"})
    public ResponseEntity<?> getProductReviews(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(productService.showAllReviews(productId));
    }
}

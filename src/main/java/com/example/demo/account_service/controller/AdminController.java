package com.example.demo.account_service.controller;

import com.example.demo.account_service.repository.AccountService;
import com.example.demo.customer_service.entity.Customer;
import com.example.demo.customer_service.repository.CustomerService;
import com.example.demo.product_service.repository.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    private AccountService accountService;
    private CustomerService customerService;
    private ProductService productService;

    public AdminController(AccountService accountService, CustomerService customerService, ProductService productService) {
        this.accountService = accountService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping("showAll")
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
}
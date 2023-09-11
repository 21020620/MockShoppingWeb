package com.example.demo.controller;

import com.example.demo.service.account.IAccountService;
import com.example.demo.service.customer.ICustomerService;
import com.example.demo.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping({"api/get/products", "service/products"})
    public String getAllProduct() {
        return productService.getAllProducts();
    }

    @GetMapping("api/get/customers")
    public String getAllCustomer() {
        return customerService.getAllCustomers();
    }
}

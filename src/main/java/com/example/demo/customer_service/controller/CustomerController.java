package com.example.demo.customer_service.controller;

import com.example.demo.account_service.entity.Account;
import com.example.demo.customer_service.entity.Customer;
import com.example.demo.customer_service.repository.CustomerService;
import com.example.demo.product_service.entity.Product;
import com.example.demo.product_service.entity.ProductReview;
import com.example.demo.product_service.repository.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("service/customers")
public class CustomerController {
    private CustomerService customerService;
    private ProductService productService;
    private static final Logger logger = Logger.getLogger(CustomerController.class.getName());

    public CustomerController(CustomerService customerService, ProductService productService) {
        this.customerService = customerService;
        this.productService = productService;
    }

    //GET METHODS
    @GetMapping("showCart")
    public ResponseEntity<?> showCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Customer customer = customerService.getCustomerByEmail(userDetails.getUsername());
        return ResponseEntity.ok(customerService.showCart(customer));
    }

    //POST METHODS
    @PostMapping( "register")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        customer.setAccount(new Account(customer.getEmail(), customer.getPassword(), "ROLE_Customer"));
        customerService.addCustomer(customer);
        System.out.println("Password: " + customer.getPassword());
        logger.info("Customer created");
        return ResponseEntity.status(201).body("Customer created");
    }

    @PostMapping("checkOut")
    public ResponseEntity<?> customerCheckOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Customer customer = customerService.getCustomerByEmail(userDetails.getUsername());

        logger.info("Customer checked out");
        return ResponseEntity.ok(customerService.checkOut(customer));
    }

    @PostMapping("addReview/{productId}")
    public ResponseEntity<?> addReview(@PathVariable Long productId, @RequestBody ProductReview productReview) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Customer customer = customerService.getCustomerByEmail(userDetails.getUsername());
        productService.addReview(productId, productReview, customer);

        return ResponseEntity.ok("Review added");
    }

    @PostMapping("addToCart")
    public ResponseEntity<?> addProductToCartI(@RequestParam Long id, @RequestParam int quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Customer customer = customerService.getCustomerByEmail(userDetails.getUsername());
        Product product = productService.getProductById(id);
        customerService.addProductToCart(customer, product, quantity);

        logger.info("Product added to cart");

        return ResponseEntity.ok("Product added to cart");
    }

    //PUT METHODS
    @PutMapping("changeDetail")
    public ResponseEntity<?> changeCustomerDetail(@RequestBody Customer inputCustomer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Customer baseCustomer = customerService.getCustomerByEmail(userDetails.getUsername());
        customerService.updateCustomer(baseCustomer, inputCustomer);

        logger.info("Customer updated");
        return ResponseEntity.ok("Customer updated");
    }
}

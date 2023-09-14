package com.example.demo.controller;

import com.example.demo.entities.ApplicationLogger;
import com.example.demo.entities.Customer;
import com.example.demo.service.customer.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class PutController {
    @Autowired
    private ICustomerService customerService;
    private static final Logger logger = ApplicationLogger.getLogger();

    @PutMapping("service/changeDetail")
    public ResponseEntity<?> changeDetail(@RequestBody Customer inputCustomer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Customer baseCustomer = customerService.getCustomerByEmail(userDetails.getUsername());
        customerService.updateCustomer(baseCustomer, inputCustomer);
        logger.info("Customer updated");
        return ResponseEntity.ok("Customer updated");
    }
}

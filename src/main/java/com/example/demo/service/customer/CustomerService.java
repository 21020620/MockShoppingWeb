package com.example.demo.service.customer;

import com.example.demo.entities.Account;
import com.example.demo.entities.ApplicationLogger;
import com.example.demo.entities.Customer;
import com.example.demo.entities.Product;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class CustomerService implements ICustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProductRepository productRepository;
    private static final Logger logger = ApplicationLogger.getLogger();

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    public void addCustomer(Customer customer) {
        Account account = new Account(customer.getEmail(), customer.getPassword(), "ROLE_Customer");
        customer.setAccount(account);
        accountRepository.save(account);
        customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        boolean exists = customerRepository.existsById(id);
        if (!exists) {
            System.out.println("Customer with id " + id + " does not exist");
        }
        customerRepository.deleteById(id);
        System.out.println("Customer removed: " + id);
    }

    public String getAllCustomers() {
        StringBuilder sb = new StringBuilder();
        customerRepository.findAll().forEach(customer -> sb.append(customer.toString()).append("\n"));
        return sb.toString();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }


    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email).orElse(null);
    }

    public void addProductToCart(Product product) {
        Product baseProduct = productRepository.findById(product.getId()).orElse(null);
        if(baseProduct == null) {
            logger.warning("Product not found");
            return;
        }else if(baseProduct.getQuantity() < product.getQuantity()) {
            logger.warning("Not enough products in stock");
            return;
        }
    }
}

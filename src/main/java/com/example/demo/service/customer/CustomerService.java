package com.example.demo.service.customer;

import ch.qos.logback.core.model.INamedModel;
import com.example.demo.entities.*;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.order.IOrderService;
import com.example.demo.service.product.IProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Logger;

@Service
public class CustomerService implements ICustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private IProductService productService;
    private static final Logger logger = ApplicationLogger.getLogger();

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void addCustomer(@Valid Customer customer) {
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
        return customerRepository.findById(id).orElseThrow(() -> new CustomerException("Customer not found"));
    }


    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow(() -> new CustomerException("Customer not found"));
    }

    @Transactional
    public void addProductToCart(Customer customer, Product product, int quantity) {
        Long productID = product.getId();
        if(product.getQuantityInStock() < quantity)
            throw new CustomerException("Not enough product in stock");
        String json = customer.getJsonCart();
        ObjectMapper objectMapper = new ObjectMapper();
        if(json == null){
            Map<Long, Integer> temp = new LinkedHashMap<>();
            try {
                if(quantity > product.getQuantityInStock())
                    throw new CustomerException("Not enough product in stock");
                temp.put(productID, quantity);
                json = objectMapper.writeValueAsString(temp);
                customer.setJsonCart(json);
                return;
            } catch (Exception e) {
                throw new CustomerException("Error when parsing json");
            }
        }
        try {
            Map<Long, Integer> shoppingCart = objectMapper.readValue(json, new TypeReference<>() {});
            if(shoppingCart.containsKey(productID)) {
                Integer quantityInCart = shoppingCart.get(product.getId());
                if(quantityInCart != null) {
                    if(quantityInCart + quantity > product.getQuantityInStock())
                        throw new CustomerException("Not enough product in stock");
                    shoppingCart.put(productID, quantityInCart + quantity);
                } else
                    shoppingCart.put(productID, quantity);
            } else {
                shoppingCart.put(productID, quantity);
            }
            customer.setJsonCart(objectMapper.writeValueAsString(shoppingCart));
        } catch (CustomerException e) {
            throw new CustomerException("Customer Exception (May not enough products)");
        } catch (Exception e) {
            throw new CustomerException("Error when parsing json");
        }
    }

    @Transactional
    public String checkOut(Customer customer) {
        StringBuilder sb = new StringBuilder();
        double totalPrice = 0;
        String json = customer.getJsonCart();
        if(json == null) return "Cart is empty";
        ObjectMapper objectMapper = new ObjectMapper();
        Set<OrderItem> orderItems = new HashSet<>();
        Order order = new Order(new Date(), orderItems, customer, totalPrice);
        try {
            Map<Long, Integer> shoppingCart = objectMapper.readValue(json, new TypeReference<>() {});
            for (Map.Entry<Long, Integer> entry : shoppingCart.entrySet()) {
                Product product = productService.getProductById(entry.getKey());
                if(product.getQuantityInStock() < entry.getValue())
                    throw new CustomerException("Not enough product in stock");
                OrderItem orderItem = new OrderItem(order, product, entry.getValue(), customer);
                sb.append("Product: ").append(product.getName()).append(" with quantity: ").append(entry.getValue()).append("\n");
                order.getOrderItems().add(orderItem);
                totalPrice += product.getPrice() * entry.getValue();
                product.setQuantityInStock(product.getQuantityInStock() - entry.getValue());
            }
            DecimalFormat df = new DecimalFormat("#.##");
            sb.append("Total price: ").append(df.format(totalPrice)).append("\n");
            order.setTotal(totalPrice);
            orderRepository.save(order);
            shoppingCart.clear();
            customer.setJsonCart(null);
        } catch (CustomerException e) {
            throw new CustomerException("Customer Exception (May not enough products)");
        } catch (Exception e) {
            throw new CustomerException("Error when parsing json");
        }
        return sb.toString();
    }

    @Transactional
    public void updateCustomer(Customer c1, Customer c2) {
        c1.setName(c2.getName() != null ? c2.getName() : c1.getName());
        c1.setAddress(c2.getAddress() != null ? c2.getAddress() : c1.getAddress());
        c1.setPhone(c2.getPhone() != null ? c2.getPhone() : c1.getPhone());
        c1.setAge(c2.getAge() != 0 ? c2.getAge() : c1.getAge());
        if(c2.getPassword() != null) {
            Account account = accountRepository.findByEmail(c2.getEmail()).orElse(null);
            if(account == null) return;
            account.setPassword(c2.getPassword());
            c1.setAccount(account);
            c1.setPassword(c2.getPassword());
        }
        if(c2.getEmail() != null) {
            Account account = accountRepository.findByEmail(c2.getEmail()).orElse(null);
            if(account == null) return;
            account.setEmail(c2.getEmail());
            c1.setAccount(account);
            c1.setEmail(c2.getEmail());
        }
    }

    public String showCart(Customer customer) {
        StringBuilder sb = new StringBuilder();
        String json = customer.getJsonCart();
        if(json == null) return "Cart is empty";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<Long, Integer> shoppingCart = objectMapper.readValue(json, new TypeReference<>() {});
            shoppingCart.forEach((key, value) -> {
                Product product = productService.getProductById(key);
                sb.append("Product: ").append(product.getName()).append(" with quantity: ").append(value).append("\n");
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }
        return sb.toString();
    }


}

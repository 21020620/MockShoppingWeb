package com.example.demo.service.customer;

import com.example.demo.entities.*;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.order.IOrderService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CustomerService implements ICustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
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
        return customerRepository.findById(id).orElse(null);
    }


    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public boolean addProductToCart(Customer customer, Product product, int quantity) {
        if(product.getQuantityInStock() < quantity) {
            logger.info("Not enough product in stock");
            return false;
        }
        if(orderService.findOrderByCustomerAndProduct(customer, product) != null) {
            Order order = orderService.findOrderByCustomerAndProduct(customer, product);
            order.setQuantity(order.getQuantity() + quantity);
            orderService.addOrder(order);
            return true;
        }
        orderService.addOrder(new Order(customer, product, quantity));
        return true;
    }

    @Transactional
    public String checkOut(Customer customer) {
        StringBuilder sb = new StringBuilder();
        double totalPrice = 0;
        List<Order> ordersOfCustomer = orderRepository.findByCustomer(customer);
        ordersOfCustomer.forEach(System.out::println);
        for(Order o : ordersOfCustomer) {
            Product product = o.getProduct();
            product.setQuantityInStock(product.getQuantityInStock() - o.getQuantity());
            totalPrice += product.getPrice() * o.getQuantity();
            sb.append("Check out for product: ").append(product.getName()).append(" with quantity: ").append(o.getQuantity()).append("\n");
            orderRepository.delete(o);
        }
        sb.append("Total price: ").append(new DecimalFormat("#").format(totalPrice));
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
        List<Order> ordersOfCustomer = orderRepository.findByCustomer(customer);
        ordersOfCustomer.forEach(order ->
            sb.append(order.getProduct().getName()).append(" with quantity: ").append(order.getQuantity()).append("\n"));
        return sb.toString();
    }

    public void validateCustomer(Customer customer) {
        if(customer.getName() == null || customer.getName().isEmpty())
            throw new CustomerException("Name cannot be empty");
    }
}

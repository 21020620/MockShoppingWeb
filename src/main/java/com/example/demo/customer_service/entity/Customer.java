package com.example.demo.customer_service.entity;

import com.example.demo.account_service.entity.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "customer")
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "Name must contain only letters")
    private String name;
    @Column(name = "age")
    @Min(value = 1, message = "Age must be greater than 0")
    private int age;
    @Column (name = "address")
    private String address;
    @Column (name = "phone", unique = true)
    @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only numbers")
    private String phone;
    @Column (name = "email", nullable = false, unique = true)
    @Email(message = "Email must be in the correct format")
    private String email;
    @Column (name = "password", nullable = false)
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_username", referencedColumnName = "email")
    private Account account;

    @Column(name = "cart")
    private String jsonCart;

    public Customer() {
    }

    public Customer(String name, int age, String address, String phone, String email, String password) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getJsonCart() {
        return jsonCart;
    }

    public void setJsonCart(String jsonCart) {
        this.jsonCart = jsonCart;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

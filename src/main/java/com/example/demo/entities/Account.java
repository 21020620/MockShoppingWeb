package com.example.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class Account {
    @Id
    private String email;
    @Transient
    private String password;

    public Account() {
    }

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
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
}

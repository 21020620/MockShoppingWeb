package com.example.demo.account_service.repository;

import com.example.demo.account_service.entity.Account;

public interface AccountService {
    void addAccount(Account account);
    void deleteAccount(String email);
    String getAllAccounts();
    Account getAccountByEmail(String email);
    boolean existsByEmail(String email);
}

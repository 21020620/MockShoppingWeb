package com.example.demo.service.account;

import com.example.demo.entities.Account;

public interface IAccountService {
    void addAccount(Account account);
    void deleteAccount(String email);
    String getAllAccounts();
    Account getAccountByEmail(String email);
    boolean existsByEmail(String email);

}

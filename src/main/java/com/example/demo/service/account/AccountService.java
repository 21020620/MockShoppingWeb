package com.example.demo.service.account;

import com.example.demo.entities.Account;
import com.example.demo.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService {
    @Autowired
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    public void addAccount(Account account) {
        accountRepository.save(account);
    }

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    public void deleteAccount(String email) {
        boolean exists = accountRepository.existsById(email);
        if (!exists) {
            System.out.println("Account with email " + email + " does not exist");
        }
        accountRepository.deleteById(email);
        System.out.println("Account removed: " + email);
    }

    @Transactional
    public String getAllAccounts() {
        StringBuilder sb = new StringBuilder();
        accountRepository.findAll().forEach(account -> sb.append(account.toString()).append("\n"));
        return sb.toString();
    }

    @Transactional
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean existsByEmail(String email) {
        return accountRepository.existsById(email);
    }
}

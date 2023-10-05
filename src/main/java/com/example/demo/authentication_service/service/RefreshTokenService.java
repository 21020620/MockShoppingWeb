package com.example.demo.authentication_service.service;

import com.example.demo.authentication_service.entity.RefreshToken;
import com.example.demo.account_service.entity.Account;
import com.example.demo.account_service.repository.AccountService;
import com.example.demo.authentication_service.repository.RefreshTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private AccountService accountService;

    public RefreshTokenService(RefreshTokenRepo refreshTokenRepo, AccountService accountService) {
        this.refreshTokenRepo = refreshTokenRepo;
        this.accountService = accountService;
    }

    public RefreshToken createRefreshToken(String email) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setAccount(accountService.getAccountByEmail(email));
        refreshToken.setExpiryDate(Instant.now().plusMillis(8640000));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshTokenRepo.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepo.delete(token);
            System.out.println("Refresh token expired");
            return null;
        }
        return token;
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepo.findByToken(token).
                orElseThrow(() -> new RuntimeException("Refresh Token is not in the database!"));
    }

    public void deleteRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepo.findByToken(token).orElse(null);
        if(refreshToken == null) {
            System.out.println("Refresh token not found");
            return;
        }
        refreshTokenRepo.delete(refreshToken);
        System.out.println("Refresh token deleted");
    }

    public RefreshToken findByAccount(Account account) {
        return refreshTokenRepo.findByAccount(account).orElse(null);
    }
}

package com.example.demo.authentication_service.controller;

import com.example.demo.account_service.entity.Account;
import com.example.demo.system_service.entity.ApplicationLogger;
import com.example.demo.account_service.repository.AccountService;
import com.example.demo.authentication_service.entity.LoginRequest;
import com.example.demo.authentication_service.entity.LoginResponse;
import com.example.demo.authentication_service.entity.JwtProvider;
import com.example.demo.authentication_service.entity.RefreshToken;
import com.example.demo.authentication_service.service.RefreshTokenService;
import com.example.demo.authentication_service.entity.TokenRefreshResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.logging.*;

@RestController
@RequestMapping()
public class AuthenticationController {
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private AccountService accountService;
    private RefreshTokenService refreshTokenService;

    private static final Logger logger = ApplicationLogger.getLogger();

    public AuthenticationController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, AccountService accountService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.accountService = accountService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtProvider.generateToken(userDetails.getUsername());
        RefreshToken checkExits = refreshTokenService.findByAccount(accountService.getAccountByEmail(userDetails.getUsername()));
        if(checkExits != null)
            refreshTokenService.deleteRefreshToken(checkExits.getToken());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
        logger.info("User logged in successfully!");
        return new LoginResponse(jwt, refreshToken.getToken());
    }

    @PostMapping(value = "/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody String refreshRequest) {
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshRequest);
        if(refreshToken == null)
            return ResponseEntity.badRequest().body("Invalid refresh token");
        if (refreshTokenService.verifyExpiration(refreshToken) == null) {
            return ResponseEntity.badRequest().body("Refresh token is expired!");
        }
        Account account = refreshToken.getAccount();
        String token = jwtProvider.generateToken(account.getEmail());
        logger.info("Token refreshed successfully!");
        return ResponseEntity.ok(new TokenRefreshResponse(token, refreshRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody Account account) {
        if (accountService.existsByEmail(account.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already taken!");
        }
        accountService.addAccount(account);
        logger.info("User registered successfully!");
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/logoutpage")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authorizationHeader.substring(7);
        JwtProvider.blackList.add(accessToken);
        String email = jwtProvider.getEmailFromJWT(accessToken);
        Account account = accountService.getAccountByEmail(email);
        RefreshToken refreshToken = refreshTokenService.findByAccount(account);
        refreshTokenService.deleteRefreshToken(refreshToken.getToken());
        logger.info("User logged out successfully!");
        return ResponseEntity.ok("User logged out successfully!");
    }
}

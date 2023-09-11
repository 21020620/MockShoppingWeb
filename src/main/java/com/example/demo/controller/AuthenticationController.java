package com.example.demo.controller;

import com.example.demo.entities.Account;
import com.example.demo.service.account.IAccountService;
import com.example.demo.service.authentication.LoginRequest;
import com.example.demo.service.authentication.LoginResponse;
import com.example.demo.service.token.JwtProvider;
import com.example.demo.service.token.RefreshToken;
import com.example.demo.service.token.RefreshTokenService;
import com.example.demo.service.token.TokenRefreshResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        Account account = accountService.getAccountByEmail(loginRequest.getUsername());
        System.out.println("Account found: " + account.toString());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));


        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtProvider.generateToken(userDetails.getUsername());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

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
        return ResponseEntity.ok(new TokenRefreshResponse(token, refreshRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody Account account) {
        if (accountService.existsByEmail(account.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already taken!");
        }
        accountService.addAccount(account);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody String refreshToken) {
        refreshTokenService.deleteRefreshToken(refreshToken);
        return ResponseEntity.ok("User logged out successfully!");
    }
}

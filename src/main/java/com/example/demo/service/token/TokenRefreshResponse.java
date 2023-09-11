package com.example.demo.service.token;

import org.springframework.stereotype.Component;

@Component
public class TokenRefreshResponse {
    private String token;
    private String refreshToken;

    public TokenRefreshResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public TokenRefreshResponse() {
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}

package com.example.demo.service;
import io.jsonwebtoken.Claims;


import javax.crypto.SecretKey;

public interface JwtService {
    SecretKey getSigningKey();
    String generateToken(Long userId);
    Long extractUserId(String token);
    boolean validateToken(String token, Long userId);
    Claims extractClaims(String token);
}

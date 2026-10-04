package com.taskflowhub.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenService {
    private final SecretKey key;
    private final long expiration;

    public JwtTokenService(@Value("${security.jwt.secret:taskflowhub-demo-secret-key-change-me-2026}") String secret, @Value("${security.jwt.expiration-ms:7200000}") long expiration) {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    public String create(Long userId, String username, String role) {
        Date now = new Date();
        return Jwts.builder().subject(username).claim("userId", userId).claim("role", role).issuedAt(now).expiration(new Date(now.getTime() + expiration)).signWith(key).compact();
    }
}

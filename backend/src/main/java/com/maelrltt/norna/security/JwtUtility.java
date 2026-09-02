package com.maelrltt.norna.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtUtility {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // Create a secret key that can be used with HMAC-SHA algorithms
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate a token based on the username
     * @param username - username of the actual user
     * @return - return a JWT Token built with the username that expires one hour after creation
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration( new Date((new Date()).getTime() + jwtExpiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Get the username from the JWT token
     * @param token - The JWT containing the username I want to retrieve
     * @return - The username I want to retrieve
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Try to validation the token or throw an exception with logs
     * @param token - Token we want to verify
     * @return - Return a boolean: whether the token is valid (true) or invalid (false).
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("JWT validation failed for token: {}", token);
            log.error(e.getMessage());

            return false;
        }
    }
}

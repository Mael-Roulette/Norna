package com.maelrltt.norna.security;

import io.jsonwebtoken.Claims;
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
    private static final String CLAIM_TYPE = "type";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long jwtRefreshExpiration;

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
        Date now = new Date();

        return Jwts.builder()
                .subject(username)
                .claim(CLAIM_TYPE, TYPE_ACCESS)
                .issuedAt(now)
                .expiration( new Date(now.getTime() + jwtExpiration))
                .signWith(secretKey)
                .compact();
    }
    public String generateRefreshToken(String username) {
        Date now = new Date();

        return Jwts.builder()
                .subject(username)
                .claim(CLAIM_TYPE, TYPE_REFRESH)
                .issuedAt(now)
                .expiration( new Date( now.getTime() + jwtRefreshExpiration ))
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
     * Get the token type ("access" or "refresh") from a token's claims
     */
    public String getTokenType(String token) {
        return parseClaims(token).get(CLAIM_TYPE, String.class);
    }

    public boolean isAccessToken(String token) {
        return TYPE_ACCESS.equals(getTokenType(token));
    }

    public boolean isRefreshToken(String token) {
        return TYPE_REFRESH.equals(getTokenType(token));
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

    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

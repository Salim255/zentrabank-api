package com.zentrabank.bank_api.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final BankApiConfigProperties config;
    private final SecretKey key;


    public JwtService(BankApiConfigProperties config){
        this.config = config;
        // Convert secret string from config into a cryptographic SecretKey
        // Required for signing JWTs. Without this, token signing fails.
        this.key = Keys.hmacShaKeyFor(config.jwtSecret().getBytes(StandardCharsets.UTF_8));
    }

    // Helper: converts a Duration (like "15m" or "7d") into a java.util.Date for JWT expiration
    // Why: JJWT requires Date objects; modern Java prefers Duration/Instant
    private Date fromNow(Duration duration) {
        // Instant.now() -> current UTC timestamp
        // plus(duration) -> adds specified time to now
        // Date.from(...) -> converts Instant to java.util.Date
        return Date.from(Instant.now().plus(duration));
    }

    /**
     * Generates a short-lived access token
     * @param userId - the ID of the authenticated user
     * @return signed JWT as a String
     */
    public String generateAccessToken(String userId) {
        return Jwts.builder()
                .subject(userId)  // JWT "sub" claim; identifies the user
                .issuedAt(Date.from(Instant.now())) // "iat" claim; when token was issued
                .expiration(Date.from(Instant.now())) // "exp" claim; token expiry
                .signWith(key) // cryptographically signs token; without this, token can be forged
                .compact(); // Builds the JWT string
    }

    /**
     * Generates a long-lived refresh token
     * @param userId - the ID of the authenticated user
     * @return signed refresh JWT as a String
     */
    public String generateRefreshToken(String userId) {
        return Jwts.builder()
                .subject(userId) // JWT "sub" claim; identifies the user
                .issuedAt(Date.from(Instant.now())) // "iat" claim
                .expiration(fromNow(config.refreshTokenExpiration())) // refresh token expires later than access token
                .signWith(key) // sign with same secret key
                .compact();
    }
}
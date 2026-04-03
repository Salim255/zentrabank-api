package com.zentrabank.bank_api.config;

import com.zentrabank.bank_api.modules.auth.dto.UserTokenDetailsDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
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

    public UserTokenDetailsDto parseToken(String token){
        // Convert your secret string into a secure HMAC key
        SecretKey key = Keys.hmacShaKeyFor(this.config.jwtSecret().getBytes(StandardCharsets.UTF_8));

        // 2. Build parser using the new API
        // Decode and validate the JWT
        JwtParser parser= Jwts.parser()
                .verifyWith(key) // your signing key
                .build();

        // 3. Parse and validate token
        Claims claims = parser.parseSignedClaims(token).getPayload();

        // Extract fields from the token payload
        String userId = claims.get("userId", String.class);
        String expireIn = claims.get("expireIn", String.class);

        // Return strongly typed DTO
        return new UserTokenDetailsDto(userId, expireIn);
    };


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
     * @param  userData - the ID of the authenticated user
     * @return signed JWT as a String
     */
    public String generateAccessToken(UserTokenDetailsDto userData) {
        return Jwts.builder()
                .subject(userData.userId())  // JWT "sub" claim; identifies the user
                .subject(userData.expireIn())
                .issuedAt(Date.from(Instant.now())) // "iat" claim; when token was issued
                .expiration(Date.from(Instant.now())) // "exp" claim; token expiry
                .signWith(key) // cryptographically signs token; without this, token can be forged
                .compact(); // Builds the JWT string
    }

    /**
     * Generates a long-lived refresh token
     * @param  userData - the ID of the authenticated user
     * @return signed refresh JWT as a String
     */
    public String generateRefreshToken(UserTokenDetailsDto userData) {
        return Jwts.builder()
                .subject(userData.userId()) // JWT "sub" claim; identifies the user
                .subject(userData.expireIn())
                .issuedAt(Date.from(Instant.now())) // "iat" claim
                .expiration(fromNow(config.refreshTokenExpiration())) // refresh token expires later than access token
                .signWith(key) // sign with same secret key
                .compact();
    }
}
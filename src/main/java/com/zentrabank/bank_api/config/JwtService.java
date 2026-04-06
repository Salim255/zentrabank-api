package com.zentrabank.bank_api.config;

import com.zentrabank.bank_api.modules.auth.dto.UserTokenDetailsDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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

        // Create secure HMAC key from secret
        this.key = Keys.hmacShaKeyFor(config.jwtSecret().getBytes(StandardCharsets.UTF_8));
    }

    // -------------------------------
    // PARSE TOKEN (NEW JJWT API)
    // -------------------------------
    public UserTokenDetailsDto parseToken(String token){

        JwtParser parser = Jwts.parser()
                .verifyWith(key)
                .build();

        Claims claims = parser.parseSignedClaims(token).getPayload();

        String userId = claims.get("userId", String.class);
        String expireIn = claims.get("expireIn", String.class);

        return new UserTokenDetailsDto(userId, expireIn);
    }

    // -------------------------------
    // GENERATE ACCESS TOKEN
    // -------------------------------
    public String generateAccessToken(String userId){
        // Method that generates a JWT token using only the userId
        return Jwts.builder()
                // Start building the token

                .subject(userId)
                // Set the subject (standard JWT field for user identity)

                .issuedAt(Date.from(Instant.now()))
                // Set creation time

                .expiration(fromNow(this.config.jwtExpiration()))
                // Set expiration time

                .signWith(key)
                // Sign the token

                .compact();
                // Return the token
    }

    // -------------------------------
    // GENERATE REFRESH TOKEN
    // -------------------------------
    public String generateRefreshToken(String userId){
        return Jwts.builder()
                .subject(userId)
                .issuedAt(Date.from(Instant.now()))
                .expiration(fromNow(config.refreshTokenExpiration()))
                .signWith(key)
                .compact();
    }

    // -------------------------------
    // Helper: convert Duration → Date
    // -------------------------------
    private Date fromNow(Duration duration){
        return Date.from(Instant.now().plus(duration));
    }
}

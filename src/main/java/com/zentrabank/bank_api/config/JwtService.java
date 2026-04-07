package com.zentrabank.bank_api.config;

import com.zentrabank.bank_api.modules.auth.dto.UserTokenDetailsDto;
import com.zentrabank.bank_api.modules.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    private final BankApiConfigProperties config;
    private final SecretKey key;
    private final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public JwtService(BankApiConfigProperties config){
        this.config = config;
        // Create secure HMAC key from secret
        this.key = Keys.hmacShaKeyFor(config.jwtSecret().getBytes(StandardCharsets.UTF_8));
    }

    // -------------------------------
    // PARSE TOKEN (NEW JJWT API)
    // -------------------------------
    public UserTokenDetailsDto parseToken(String token){
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(key)
                    .build();

            Claims claims = parser.parseSignedClaims(token).getPayload();

            String userIdStr = claims.getSubject();
            UUID userId = UUID.fromString(userIdStr);
            String expireIn = claims.get("exp", Long.class).toString();
            String roleStr = claims.get("role", String.class);

            UserRole role = UserRole.valueOf(roleStr);

            return new UserTokenDetailsDto(userId, expireIn, role);
        } catch (Exception e) {
            logger.error("Parse token error{}", e.getMessage());
            throw new RuntimeException("Invalid role in token");
        }

    }

    // -------------------------------
    // GENERATE ACCESS TOKEN
    // -------------------------------
    public String generateAccessToken(String userId, UserRole role){
        // Method that generates a JWT token using only the userId
        return Jwts.builder()
                // Start building the token

                .subject(userId)
                // Set the subject (standard JWT field for user identity)
                .claim("role",role.toString())
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
    public String generateRefreshToken(String userId, UserRole role){
        return Jwts.builder()
                .subject(userId)
                .claim("role",role.toString())
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

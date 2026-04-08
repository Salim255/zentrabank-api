package com.zentrabank.bank_api.modules.refreshtoken.servce;

import com.zentrabank.bank_api.config.JwtService;
import com.zentrabank.bank_api.exceptions.UnauthorizedException;
import com.zentrabank.bank_api.modules.refreshtoken.dto.CreateTokenDto;
import com.zentrabank.bank_api.modules.refreshtoken.dto.RefreshTokenDto;
import com.zentrabank.bank_api.modules.refreshtoken.entity.RefreshToken;
import com.zentrabank.bank_api.modules.refreshtoken.repository.RefreshTokenRepository;
import com.zentrabank.bank_api.modules.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
public class RefreshTokenServiceImp implements RefreshTokenService {
    @PersistenceContext // Injects the JPA EntityManager
    private EntityManager entityManager;

    private final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImp.class);
    private final RefreshTokenRepository refreshTokenRepository;


    public RefreshTokenServiceImp(
            JwtService jwtService,
            RefreshTokenRepository refreshTokenRepository
    ){
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override()
    public void deleteExpiredTokens(){
        try {
            // Step 1: Delete all expired tokens
            // These tokens are useless because they are no longer valid anyway
            this.refreshTokenRepository.deleteByExpiresAtBefore(Instant.now());

            //  Step 2: Delete old revoked tokens
            // - revoked = already invalid (user logged out or token rotated)
            // - we keep them temporarily for security/audit
            // - here we remove ones older than 7 days
            Instant cutoff = Instant.now().minus(7, ChronoUnit.DAYS);
            this.refreshTokenRepository.deleteByRevokedTrueAndCreatedAtBefore(cutoff);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RefreshTokenDto findTokenByToken(String refreshToken){
        try {
            RefreshToken token =  this.refreshTokenRepository
                    .findByToken(refreshToken).
                    orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
            return new RefreshTokenDto(token.getId(), token.getToken(), token.getExpiresAt(), token.isRevoked());
        } catch (Exception ex) {
            this.logger.error("Error find token { } 🔥🔥🔥", ex);
            throw ex;
        }
    }

    @Override
    public void createRefreshToken(CreateTokenDto payload){
        try {
            // Get a reference (proxy) to the User entity WITHOUT querying the database
            // Hibernate will NOT execute a SELECT here
            // It simply creates a lightweight proxy object with only the ID set
            // This is enough because we only need the user_id for the foreign key
            User userRef = entityManager.getReference(User.class, payload.user_id());

            RefreshToken token = RefreshToken
                    .builder()
                    .user(userRef)
                    .token(payload.token())
                    .expiresAt(payload.expiresAt())
                    .build();

            this.refreshTokenRepository.save(token);
        } catch (Exception e) {
            this.logger.error("Error on create refresh token { } 🔥🔥🔥", e);
            throw e;
        }
    }

    @Override
    public void removeRefreshToken(String refreshToken) {
        try {
            this.refreshTokenRepository.deleteByToken(refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
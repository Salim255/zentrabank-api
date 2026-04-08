package com.zentrabank.bank_api.modules.refreshtoken.repository;
import com.zentrabank.bank_api.modules.refreshtoken.dto.CreateTokenDto;
import com.zentrabank.bank_api.modules.refreshtoken.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    @Modifying
    @Transactional
    @Query(
            value = "UPDATE refresh_tokens AS rft SET rft.revoked = true WHERE rft.token = :token;",
            nativeQuery = true
    )
    int revokeToken(@Param("token") String token);

    void deleteByRevokedTrueAndCreatedAtBefore(Instant cutoff);
    void deleteByExpiresAtBefore(Instant currentTime);
    void deleteByToken(String token);
    Optional<RefreshToken> findByToken(String token);
}
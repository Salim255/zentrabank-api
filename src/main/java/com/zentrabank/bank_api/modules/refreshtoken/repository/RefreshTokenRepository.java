package com.zentrabank.bank_api.modules.refreshtoken.repository;
import com.zentrabank.bank_api.modules.refreshtoken.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    void deleteByToken(String token);

    Optional<RefreshToken> findByToken(String token);
}
package com.zentrabank.bank_api.modules.refreshtoken.dto;

import java.time.Instant;
import java.util.UUID;

public record RefreshTokenDto(
        UUID tokenId,
        String token,
        Instant expiresAt,
        boolean revoked
) {
}
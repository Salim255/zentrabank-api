package com.zentrabank.bank_api.modules.refreshtoken.dto;

import java.time.Instant;
import java.util.UUID;

public record CreateTokenDto(
        UUID user_id,
        String token,
        Instant expiresAt
) {
}
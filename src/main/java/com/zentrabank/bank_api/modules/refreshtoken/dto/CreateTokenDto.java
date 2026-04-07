package com.zentrabank.bank_api.modules.refreshtoken.dto;

import java.time.Instant;

public record CreateTokenDto(
        String token,
        Instant expiresAt
) {
}
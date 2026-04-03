package com.zentrabank.bank_api.modules.auth.dto;

import java.time.Instant;
import java.util.UUID;

public record RegisterResponseDto(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String role,
        int loginId,
        int tempPassword,
        Instant createdAt,
        Instant updatedAt
) { }
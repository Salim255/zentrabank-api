package com.zentrabank.bank_api.modules.auth.dto;

import com.zentrabank.bank_api.modules.user.entity.UserRole;

public record LoginResponseDto(
        String userId,
        String email,
        String lastName,
        String firstName,
        boolean firstLogin,
        UserRole role
        ) {
}
package com.zentrabank.bank_api.modules.auth.dto;

import com.zentrabank.bank_api.modules.user.entity.UserRole;

import java.util.UUID;

public record LoggedUserDto(
        UUID userId,
        String email,
        boolean firstLogin,
        UserRole role
) { }
package com.zentrabank.bank_api.modules.auth.dto;

import com.zentrabank.bank_api.modules.user.entity.UserRole;

import java.util.UUID;

public record UserTokenDetailsDto(
        UUID userId,
        String expireIn,
        UserRole role
) { }
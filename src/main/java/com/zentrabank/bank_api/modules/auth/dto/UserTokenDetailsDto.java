package com.zentrabank.bank_api.modules.auth.dto;

import com.zentrabank.bank_api.modules.user.entity.UserRole;

public record UserTokenDetailsDto(
        String userId,
        String expireIn,
        UserRole role
) { }
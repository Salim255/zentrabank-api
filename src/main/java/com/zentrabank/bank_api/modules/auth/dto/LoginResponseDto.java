package com.zentrabank.bank_api.modules.auth.dto;

import com.zentrabank.bank_api.modules.user.entity.UserRole;

public record LoginResponseDto(
        LoggedUserDto user,
        TokenDto tokens
        ) {
}
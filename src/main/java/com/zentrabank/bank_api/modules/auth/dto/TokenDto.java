package com.zentrabank.bank_api.modules.auth.dto;

public record TokenDto(
        String accessToken,
        String refreshToken
) {}
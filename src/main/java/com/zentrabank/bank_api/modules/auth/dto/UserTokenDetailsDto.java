package com.zentrabank.bank_api.modules.auth.dto;

public record UserTokenDetailsDto(
        String userId,
        String expireIn
) { }
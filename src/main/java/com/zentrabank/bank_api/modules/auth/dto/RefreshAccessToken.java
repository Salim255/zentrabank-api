package com.zentrabank.bank_api.modules.auth.dto;

import com.zentrabank.bank_api.modules.auth.dto.TokenDto;

public record RefreshAccessToken(
        TokenDto tokens
) { }
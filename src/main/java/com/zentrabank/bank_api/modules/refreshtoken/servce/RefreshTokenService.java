package com.zentrabank.bank_api.modules.refreshtoken.servce;

import com.zentrabank.bank_api.modules.refreshtoken.dto.CreateTokenDto;
import com.zentrabank.bank_api.modules.refreshtoken.dto.RefreshTokenDto;

public interface RefreshTokenService {
    public void createRefreshToken(CreateTokenDto payload);
    public void removeRefreshToken(String refreshToken);
}
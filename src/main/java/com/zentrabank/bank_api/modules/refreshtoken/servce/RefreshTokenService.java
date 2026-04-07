package com.zentrabank.bank_api.modules.refreshtoken.servce;

import com.zentrabank.bank_api.modules.refreshtoken.dto.RefreshTokenDto;

public interface RefreshTokenService {
    public RefreshTokenDto createRefreshToken();
    public  void  removeRefreshToken(String refreshToken);
}
package com.zentrabank.bank_api.modules.refreshtoken.servce;

import com.zentrabank.bank_api.config.JwtService;
import com.zentrabank.bank_api.modules.refreshtoken.dto.CreateTokenDto;
import com.zentrabank.bank_api.modules.refreshtoken.dto.RefreshTokenDto;
import com.zentrabank.bank_api.modules.refreshtoken.entity.RefreshToken;
import com.zentrabank.bank_api.modules.refreshtoken.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;


@Service
public class RefreshTokenServiceImp implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private  final JwtService jwtService;

    public RefreshTokenServiceImp(
            JwtService jwtService,
            RefreshTokenRepository refreshTokenRepository
    ){
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void createRefreshToken(CreateTokenDto payload){
        try {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(payload.token());
            refreshToken.setExpiresAt(payload.expiresAt());

            this.refreshTokenRepository.save(refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeRefreshToken(String refreshToken) {
        try {
            this.refreshTokenRepository.deleteByToken(refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
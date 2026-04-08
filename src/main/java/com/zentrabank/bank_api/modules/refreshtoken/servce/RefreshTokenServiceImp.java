package com.zentrabank.bank_api.modules.refreshtoken.servce;

import com.zentrabank.bank_api.config.JwtService;
import com.zentrabank.bank_api.exceptions.UnauthorizedException;
import com.zentrabank.bank_api.modules.refreshtoken.dto.CreateTokenDto;
import com.zentrabank.bank_api.modules.refreshtoken.dto.RefreshTokenDto;
import com.zentrabank.bank_api.modules.refreshtoken.entity.RefreshToken;
import com.zentrabank.bank_api.modules.refreshtoken.repository.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class RefreshTokenServiceImp implements RefreshTokenService {
    private final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImp.class);
    private final RefreshTokenRepository refreshTokenRepository;


    public RefreshTokenServiceImp(
            JwtService jwtService,
            RefreshTokenRepository refreshTokenRepository
    ){
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshTokenDto findTokenByToken(String refreshToken){
        try {
            RefreshToken token =  this.refreshTokenRepository
                    .findByToken(refreshToken).
                    orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
            return new RefreshTokenDto(token.getId(), token.getToken(), token.getExpiresAt(), token.isRevoked());
        } catch (Exception ex) {
            this.logger.error("Error find token { }", ex);
            throw new RuntimeException(ex);
        }
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
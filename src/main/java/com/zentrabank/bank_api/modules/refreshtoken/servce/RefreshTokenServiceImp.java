package com.zentrabank.bank_api.modules.refreshtoken.servce;

import com.zentrabank.bank_api.config.JwtService;
import com.zentrabank.bank_api.modules.refreshtoken.dto.RefreshTokenDto;
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
    public RefreshTokenDto createRefreshToken(){
        try {
            String refreshToken = this.jwtService.generateRefreshToken(userId, user.getRole());
            RefreshTokenDto token = this.refreshTokenRepository.findBy();
            return  token;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeRefreshToken(String refreshToken) {
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
package com.zentrabank.bank_api.modules.auth.service;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.modules.auth.dto.*;

import java.util.UUID;


public interface AuthService {
    public RefreshAccessToken refreshAccessToken(String refreshToken);
    public ApiResponseDto<String> resetPassword(ResetPasswordDto payload, UUID userId);
    public ApiResponseDto<RegisterResponseDto> register(RegisterDto payload);
    public ApiResponseDto<LoginResponseDto> login(LoginDto payload);
}
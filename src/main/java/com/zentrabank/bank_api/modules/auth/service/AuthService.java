package com.zentrabank.bank_api.modules.auth.service;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.modules.auth.dto.*;


public interface AuthService {
    public ApiResponseDto<String> resetPassword(ResetPasswordDto payload);
    public ApiResponseDto<RegisterResponseDto> register(RegisterDto payload);
    public  ApiResponseDto<LoginResponseDto> login(LoginDto payload);
}
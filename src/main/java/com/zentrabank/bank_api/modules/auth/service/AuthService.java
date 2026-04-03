package com.zentrabank.bank_api.modules.auth.service;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.modules.auth.dto.LoginDto;
import com.zentrabank.bank_api.modules.auth.dto.LoginResponseDto;
import com.zentrabank.bank_api.modules.auth.dto.RegisterDto;
import com.zentrabank.bank_api.modules.auth.dto.RegisterResponseDto;


public interface AuthService {
    public String resetPassword(String message);
    public ApiResponseDto<RegisterResponseDto> register(RegisterDto payload);
    public  ApiResponseDto<LoginResponseDto> login(LoginDto payload);
}
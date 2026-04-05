package com.zentrabank.bank_api.modules.auth.controller;
import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.common.utils.JwtCookieUtils;
import com.zentrabank.bank_api.modules.auth.dto.*;
import com.zentrabank.bank_api.modules.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponseDto<LoginResponseDto> Login(
            @Valid @RequestBody LoginDto body,
            HttpServletResponse response
    ){
        ApiResponseDto<LoginResponseDto> responseDto = this.authService.login(body);
        Cookie cookie = JwtCookieUtils.createJwtCookie(responseDto.data()., false, 24*60*60);
        response.addCookie(cookie);
        return  this.authService.login(body);
    }
}
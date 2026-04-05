package com.zentrabank.bank_api.modules.auth.controller;
import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.modules.auth.dto.*;
import com.zentrabank.bank_api.modules.auth.service.AuthService;
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

    @PatchMapping("/reset-password")
    public ApiResponseDto<String> resetPassword(
      @Valid @RequestBody ResetPasswordDto body,
       HttpServletResponse response,
      Authentication auth
    ){

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }


        UUID userId = (UUID) auth.getPrincipal();
        return  this.authService.resetPassword(body, userId);
    }

    @PostMapping("/register")
    public ApiResponseDto<RegisterResponseDto> register(
            @Valid @RequestBody RegisterDto body,
            HttpServletResponse response
    ){
        return this.authService.register(body);
    }

    @PostMapping("/login")
    public ApiResponseDto<LoginResponseDto> Login(
            @Valid @RequestBody LoginDto body,
            HttpServletResponse response
    ){
        return  this.authService.login(body);
    }
}
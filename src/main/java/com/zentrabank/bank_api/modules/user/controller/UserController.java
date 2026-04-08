package com.zentrabank.bank_api.modules.user.controller;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.exceptions.ForbiddenException;
import com.zentrabank.bank_api.exceptions.UnauthorizedException;
import com.zentrabank.bank_api.modules.auth.dto.RegisterDto;
import com.zentrabank.bank_api.modules.auth.dto.RegisterResponseDto;
import com.zentrabank.bank_api.modules.auth.dto.ResetPasswordDto;
import com.zentrabank.bank_api.modules.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService){
        this.authService = authService;
    }

    @GetMapping("/{userId}")
    public String getUser(@PathVariable String userId){
        return  "Hello form get user" + userId;
    }

    @PatchMapping("/reset-password")
    public ApiResponseDto<String> resetPassword(
            @Valid @RequestBody ResetPasswordDto body,
            HttpServletResponse response,
            Authentication auth
    ){

        if (auth == null || !auth.isAuthenticated()) {
            throw new ForbiddenException("Invalid authentication token");
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
}
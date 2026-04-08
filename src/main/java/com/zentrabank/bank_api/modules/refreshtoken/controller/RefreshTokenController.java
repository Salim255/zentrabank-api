package com.zentrabank.bank_api.modules.refreshtoken.controller;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.exceptions.ForbiddenException;
import com.zentrabank.bank_api.exceptions.UnauthorizedException;
import com.zentrabank.bank_api.modules.refreshtoken.servce.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/refresh-tokens")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PatchMapping("/logout")
    public ApiResponseDto<String> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication auth
    ){
        if (auth == null || !auth.isAuthenticated()) {
            throw new ForbiddenException("Invalid authentication token");
        }

        // 1 Revoke token

        this.refreshTokenService.revokeToken();
        return ApiResponseDto.success("Logout with success");
    }
}
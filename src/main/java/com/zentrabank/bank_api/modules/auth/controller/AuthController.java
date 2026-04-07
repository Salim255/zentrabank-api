package com.zentrabank.bank_api.modules.auth.controller;
import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.common.utils.JwtCookieUtils;
import com.zentrabank.bank_api.config.BankApiConfigProperties;
import com.zentrabank.bank_api.config.JwtService;
import com.zentrabank.bank_api.exceptions.UnauthorizedException;
import com.zentrabank.bank_api.modules.auth.dto.*;
import com.zentrabank.bank_api.modules.auth.service.AuthService;
import com.zentrabank.bank_api.modules.user.entity.UserRole;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final BankApiConfigProperties configProperties;
    private final AuthService authService;

    public AuthController(
            JwtService jwtService,
            BankApiConfigProperties configProperties,
            AuthService authService
    ){
        this.jwtService  = jwtService;
        this.authService = authService;
        this.configProperties = configProperties;
    }

    @GetMapping("/refresh-token")
    public ApiResponseDto<LoginResponseDto> refreshToken(
            HttpServletRequest request
    ){
        // 1 Get token
        String refreshToken = JwtCookieUtils.extractJwt(request, "zentra_refresh_jwt");

        // 2 Check token
        if (refreshToken == null) throw new UnauthorizedException("No refresh token");

        // 3 Parse token data
        UserTokenDetailsDto tokenData = this.jwtService.parseToken(refreshToken);

        // Get user Id
        UUID userId = tokenData.userId();
        UserRole role = tokenData.role();
    }

    @PostMapping("/login")
    public ApiResponseDto<LoginResponseDto> Login(
            @Valid @RequestBody LoginDto body,
            HttpServletResponse response
    ){
        ApiResponseDto<LoginResponseDto> responseDto = this.authService.login(body);
        Cookie accessToeknCookie = JwtCookieUtils
                .createJwtCookie(
                        responseDto.data().tokens().accessToken(),
                        false,
                        24*60*60,
                        configProperties.accessJwtName()
                        );
        Cookie refreshTokenCookie = JwtCookieUtils
                .createJwtCookie(
                        responseDto.data().tokens().refreshToken(),
                        false,
                        24*60*60,
                        configProperties.refreshJwtName()
                        );
        response.addCookie(accessToeknCookie);
        response.addCookie(refreshTokenCookie);

        return responseDto;
    }
}
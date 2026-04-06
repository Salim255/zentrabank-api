package com.zentrabank.bank_api.modules.auth.controller;
import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.common.utils.JwtCookieUtils;
import com.zentrabank.bank_api.modules.auth.dto.*;
import com.zentrabank.bank_api.modules.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
        Cookie accessToeknCookie = JwtCookieUtils
                .createJwtCookie(
                        responseDto.data().tokens().accessToken(),
                        false,
                        24*60*60);
        Cookie refreshTokenCookie = JwtCookieUtils
                .createJwtCookie(
                        responseDto.data().tokens().refreshToken(),
                        false,
                        24*60*60);
        response.addCookie(accessToeknCookie);
        response.addCookie(refreshTokenCookie);

        return responseDto;
    }
}
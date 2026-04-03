package com.zentrabank.bank_api.modules.auth.controller;
import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.modules.auth.dto.LoginDto;
import com.zentrabank.bank_api.modules.auth.dto.LoginResponseDto;
import com.zentrabank.bank_api.modules.auth.dto.RegisterDto;
import com.zentrabank.bank_api.modules.auth.dto.RegisterResponseDto;
import com.zentrabank.bank_api.modules.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
            this.authService = authService;
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
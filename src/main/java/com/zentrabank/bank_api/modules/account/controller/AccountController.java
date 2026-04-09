package com.zentrabank.bank_api.modules.account.controller;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.exceptions.ForbiddenException;
import com.zentrabank.bank_api.modules.account.dto.CreateAccountDto;
import com.zentrabank.bank_api.modules.account.dto.CreateAccountResponseDto;
import com.zentrabank.bank_api.modules.account.service.AccountService;
import com.zentrabank.bank_api.modules.auth.dto.LoginDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping()
    public ApiResponseDto<CreateAccountResponseDto> createAccount(
            @Valid @RequestBody CreateAccountDto body,
            Authentication auth
    ){
        // Validate auth
        if (auth == null || !auth.isAuthenticated()) {
            throw new ForbiddenException("Invalid authentication token");
        }

        // Get userId
        UUID userId = (UUID) auth.getPrincipal();

        // Create and return
        return ApiResponseDto.success(this.accountService.createAccount(body, userId));
    }

    public  String updateAccount(){
        return  "hello from update account";
    }

    public  String blockAccount(){
        return  "Hello from block account";
    }

    public  String deleteAccount(){
        return  "Hello from delete account";
    }

}
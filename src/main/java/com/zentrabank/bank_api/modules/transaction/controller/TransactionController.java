package com.zentrabank.bank_api.modules.transaction.controller;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.exceptions.ForbiddenException;
import com.zentrabank.bank_api.modules.account.dto.CreateAccountDto;
import com.zentrabank.bank_api.modules.transaction.dto.CreateTransactionDto;
import com.zentrabank.bank_api.modules.transaction.dto.TransactionResponseDto;
import com.zentrabank.bank_api.modules.transaction.dto.TransferDto;
import com.zentrabank.bank_api.modules.transaction.entity.TransactionType;
import com.zentrabank.bank_api.modules.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("transfer")
    public ApiResponseDto<TransactionResponseDto> transfer(
            @Valid @RequestBody TransferDto body,
            Authentication auth
    ){
        // Validate auth
        if (auth == null || !auth.isAuthenticated()) {
            throw new ForbiddenException("Invalid authentication token");
        }

        // Get userId
        UUID userId = (UUID) auth.getPrincipal();

        // Create and return
        TransactionResponseDto response = transactionService.transferOperation(body, userId);;

        return ApiResponseDto.success(response);
    }

    @PostMapping()
    public ApiResponseDto<TransactionResponseDto> createTransaction(
            @Valid @RequestBody CreateTransactionDto body,
            Authentication auth
    ){
        // Validate auth
        if (auth == null || !auth.isAuthenticated()) {
            throw new ForbiddenException("Invalid authentication token");
        }

        // Get userId
        UUID userId = (UUID) auth.getPrincipal();

        // Create and return
        TransactionResponseDto response;
        switch (body.type()) {
            case DEPOSIT -> response = transactionService.depositOperation(body, userId);
            case WITHDRAWAL -> response = transactionService.withdrawalOperation(body, userId);
            default -> throw new IllegalArgumentException("Unsupported transaction type");
        }
        return ApiResponseDto.success(response);
    }
}
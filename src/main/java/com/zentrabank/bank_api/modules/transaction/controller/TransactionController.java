package com.zentrabank.bank_api.modules.transaction.controller;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.exceptions.ForbiddenException;
import com.zentrabank.bank_api.modules.account.dto.CreateAccountDto;
import com.zentrabank.bank_api.modules.transaction.dto.CreateTransactionDto;
import com.zentrabank.bank_api.modules.transaction.dto.GetTransactionsResponseDto;
import com.zentrabank.bank_api.modules.transaction.dto.TransactionResponseDto;
import com.zentrabank.bank_api.modules.transaction.dto.TransferDto;
import com.zentrabank.bank_api.modules.transaction.entity.TransactionType;
import com.zentrabank.bank_api.modules.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;


    // ---------------------------------------------------------
    // GET ALL TRANSACTIONS FOR AUTHENTICATED USER
    // ---------------------------------------------------------
    @GetMapping
    @Operation(
            summary = "Get user transactions",
            description = "Fetches all transactions belonging to the authenticated user. "
                    + "Supports optional pagination parameters.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transactions retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    )
            }
    )
    public ApiResponseDto<GetTransactionsResponseDto> getUserTransactions(
            Authentication auth,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) int size
    ) {
        UUID userId = (UUID) auth.getPrincipal();
        return ApiResponseDto.success(
                transactionService.getTransactionsForAccount(userId, page, size)
        );
    }

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
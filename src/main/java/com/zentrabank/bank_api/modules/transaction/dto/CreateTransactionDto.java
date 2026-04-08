package com.zentrabank.bank_api.modules.transaction.dto;

import com.zentrabank.bank_api.modules.transaction.entity.TransactionType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateTransactionDto(
        @NotBlank(message = "Description is required")
        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        BigDecimal amount,

        @NotNull(message = "Transaction type is required")
        TransactionType type,

        @NotBlank(message = "Reference account number is required")
        @Pattern(regexp = "\\d{9}", message = "Account number must be 9 digits")
        String referenceAccountNumber
) { }
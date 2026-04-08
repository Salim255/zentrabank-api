package com.zentrabank.bank_api.modules.account.dto;

import com.zentrabank.bank_api.modules.account.entity.Account.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateAccountDto(
        @Schema(example = "CHECKING")
        @NotNull(message = "Account type is required")
        AccountType accountType
) { }
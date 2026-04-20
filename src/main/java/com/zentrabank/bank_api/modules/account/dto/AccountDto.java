package com.zentrabank.bank_api.modules.account.dto;

import com.zentrabank.bank_api.modules.account.entity.Account.AccountStatus;
import com.zentrabank.bank_api.modules.account.entity.Account.AccountType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AccountDto(
        UUID id,
        String accountNumber,
        BigDecimal balance,
        AccountType type,
        AccountStatus status,
        String currency,
        String iban,
        String bic,
        boolean overdraftEnabled,
        BigDecimal overdraftLimit,
        Instant createdAt,
        Instant updatedAt
) { }
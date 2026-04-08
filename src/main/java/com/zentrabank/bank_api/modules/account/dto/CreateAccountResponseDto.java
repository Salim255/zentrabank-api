package com.zentrabank.bank_api.modules.account.dto;

import com.zentrabank.bank_api.modules.account.entity.Account.AccountStatus;
import com.zentrabank.bank_api.modules.account.entity.Account.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateAccountResponseDto(
        UUID id,
        String  accountNumber,
        BigDecimal balance,
        AccountType accountType,
        AccountStatus accountStatus,
        String currency,
        boolean overdraftEnabled,
        BigDecimal overdraftLimit

) {
}
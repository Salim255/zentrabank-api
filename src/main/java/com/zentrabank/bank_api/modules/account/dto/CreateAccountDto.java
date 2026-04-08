package com.zentrabank.bank_api.modules.account.dto;

import com.zentrabank.bank_api.modules.account.entity.Account.AccountType;

import java.util.UUID;

public record CreateAccountDto(
        UUID userId,
        AccountType accountType
) { }
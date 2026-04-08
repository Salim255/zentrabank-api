package com.zentrabank.bank_api.modules.account.dto;

import com.zentrabank.bank_api.modules.account.entity.Account.AccountType;

public record CreateAccountDto(
        AccountType accountType
) { }
package com.zentrabank.bank_api.modules.account.service;

import com.zentrabank.bank_api.modules.account.dto.CreateAccountDto;
import com.zentrabank.bank_api.modules.account.dto.CreateAccountResponseDto;

import java.util.UUID;

public interface AccountService {
    public CreateAccountResponseDto createAccount(CreateAccountDto payload, UUID userId);
    public String updateAccount();
    public String blockAccount();
    public String deleteAccount();
}
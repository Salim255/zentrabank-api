package com.zentrabank.bank_api.modules.account.service;

import com.zentrabank.bank_api.modules.account.dto.CreateAccountDto;
import com.zentrabank.bank_api.modules.account.dto.CreateAccountResponseDto;
import com.zentrabank.bank_api.modules.account.entity.Account.Account;

import java.util.UUID;

public interface AccountService {
    public void saveAccountChange(Account account);
    public Account findAccountByAccountNumber(String accountNumber);
    public CreateAccountResponseDto createAccount(CreateAccountDto payload, UUID userId);
    public String updateAccount();
    public String blockAccount();
    public String deleteAccount();
    public Account findAccountByUserId(UUID userId);
}
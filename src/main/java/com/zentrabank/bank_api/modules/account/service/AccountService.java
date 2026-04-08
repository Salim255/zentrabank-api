package com.zentrabank.bank_api.modules.account.service;

import com.zentrabank.bank_api.modules.account.dto.CreateAccountDto;

public interface AccountService {
    public String createAccount(CreateAccountDto payload);
    public String updateAccount();
    public String blockAccount();
    public String deleteAccount();
}
package com.zentrabank.bank_api.modules.account.service;

import com.zentrabank.bank_api.modules.account.dto.AccountDto;
import com.zentrabank.bank_api.modules.account.entity.Account.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountDto toDto(Account account) {
        return new AccountDto(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getType(),
                account.getStatus(),
                account.getCurrency(),
                account.getIban(),
                account.getBic(),
                account.isOverdraftEnabled(),
                account.getOverdraftLimit(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }
}
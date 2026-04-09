package com.zentrabank.bank_api.modules.transaction.dto;

import com.zentrabank.bank_api.modules.account.entity.Account.Account;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LockedAccountsDto {
    private final Account sender;
    private final Account receiver;

    public Account getSender() {
        return sender;
    }
    public Account getReceiver() {
        return receiver;
    }
}
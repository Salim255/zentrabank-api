package com.zentrabank.bank_api.modules.transaction.dto;

import com.zentrabank.bank_api.modules.account.entity.Account.Account;
import com.zentrabank.bank_api.modules.transaction.entity.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionDto(
        UUID id,
        TransactionType type,
        BigDecimal amount,
        String reason,
        BigDecimal postTransactionBalance,
        String toClientName,
        String toAccountIbn,
        Instant createdAt
) { }
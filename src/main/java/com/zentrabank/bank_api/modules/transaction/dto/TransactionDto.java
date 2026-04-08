package com.zentrabank.bank_api.modules.transaction.dto;

import com.zentrabank.bank_api.modules.transaction.entity.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionDto(
        UUID id,
        TransactionType type,
        BigDecimal amount,
        String currency,
        String referenceAccountNumber,
        BigDecimal postTransactionBalance
) {
}
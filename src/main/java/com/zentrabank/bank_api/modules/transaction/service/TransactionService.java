package com.zentrabank.bank_api.modules.transaction.service;

import com.zentrabank.bank_api.modules.transaction.dto.CreateTransactionDto;
import com.zentrabank.bank_api.modules.transaction.dto.TransactionResponseDto;

import java.util.UUID;

public interface TransactionService {
    public TransactionResponseDto createTransaction(CreateTransactionDto payload, UUID userId);
}
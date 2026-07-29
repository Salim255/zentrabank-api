package com.zentrabank.bank_api.modules.transaction.service;

import com.zentrabank.bank_api.modules.transaction.dto.*;

import java.util.UUID;

public interface TransactionService {
    public AccountTransactionsResponseDto getTransactionsForAccount(UUID userId, int page, int size);
    public TransactionResponseDto  transferOperation(TransferDto payload, UUID userId);
    public TransactionResponseDto withdrawalOperation(CreateTransactionDto payload, UUID userId);
    public TransactionResponseDto depositOperation(CreateTransactionDto payload, UUID userId);
}
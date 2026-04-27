package com.zentrabank.bank_api.modules.transaction.service;

import com.zentrabank.bank_api.modules.transaction.dto.CreateTransactionDto;
import com.zentrabank.bank_api.modules.transaction.dto.TransactionResponseDto;
import com.zentrabank.bank_api.modules.transaction.dto.TransferDto;

import java.util.UUID;

public interface TransactionService {
    public  TransactionResponseDto  transferOperation(TransferDto payload, UUID userId);
    public TransactionResponseDto withdrawalOperation(CreateTransactionDto payload, UUID userId);
    public TransactionResponseDto depositOperation(CreateTransactionDto payload, UUID userId);
}
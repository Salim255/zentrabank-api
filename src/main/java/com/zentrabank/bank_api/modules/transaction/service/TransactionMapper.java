package com.zentrabank.bank_api.modules.transaction.service;

import com.zentrabank.bank_api.modules.transaction.dto.TransactionDto;
import com.zentrabank.bank_api.modules.transaction.entity.Transaction;

public class TransactionMapper {
    public TransactionDto toDto(Transaction transaction){
            return new TransactionDto(
                    transaction.getId(),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getPostTransactionBalance(),
                    transaction.getCreatedAt()
            );
    }
}
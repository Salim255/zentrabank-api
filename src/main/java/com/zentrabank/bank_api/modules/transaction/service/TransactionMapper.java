package com.zentrabank.bank_api.modules.transaction.service;

import com.zentrabank.bank_api.modules.transaction.dto.TransactionDto;
import com.zentrabank.bank_api.modules.transaction.entity.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class TransactionMapper {
    public TransactionDto toDto(Transaction transaction){
            return new TransactionDto(
                    transaction.getId(),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getTransfer().getDescription(),
                    transaction.getPostTransactionBalance(),
                    transaction.getTransfer().getExternalRecipientName(),
                    transaction.getTransfer().getToAccount().getIban(),
                    transaction.getCreatedAt()
            );
    }
}
package com.zentrabank.bank_api.modules.transaction.service;


import com.zentrabank.bank_api.modules.account.entity.Account.Account;
import com.zentrabank.bank_api.modules.account.service.AccountService;
import com.zentrabank.bank_api.modules.transaction.dto.CreateTransactionDto;
import com.zentrabank.bank_api.modules.transaction.dto.TransactionResponseDto;
import com.zentrabank.bank_api.modules.transaction.entity.Transaction;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionService {
    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImp.class);
    private final  EntityManager entityManager;
    private AccountService accountService;
    //private final  Transa

    public TransactionResponseDto createTransaction(
            CreateTransactionDto payload,
            UUID userId
    ){
        try {
            // 1 Validate transaction payload

            // 2 Get current account reference!
            Account account = this.accountService.findAccountByUserId(userId);
            Account accountRef = entityManager.getReference(Account.class, account.getId());

            // Create transaction
            Transaction transaction = new Transaction();
            transaction.setAccount(accountRef);
            transaction.setAmount(payload.amount());
            transaction.setDescription(payload.description());
            transaction.setType(payload.type());
            transaction.setReferenceAccountNumber(payload.referenceAccountNumber());



        } catch (Exception e) {
            this.logger.error("Error to create transaction { }", e);
            throw e;
        }
    }
}
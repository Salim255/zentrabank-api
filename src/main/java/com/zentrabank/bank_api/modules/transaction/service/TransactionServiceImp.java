package com.zentrabank.bank_api.modules.transaction.service;


import com.zentrabank.bank_api.modules.account.entity.Account.Account;
import com.zentrabank.bank_api.modules.account.service.AccountService;
import com.zentrabank.bank_api.modules.transaction.dto.CreateTransactionDto;
import com.zentrabank.bank_api.modules.transaction.dto.TransactionDto;
import com.zentrabank.bank_api.modules.transaction.dto.TransactionResponseDto;
import com.zentrabank.bank_api.modules.transaction.entity.Transaction;
import com.zentrabank.bank_api.modules.transaction.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionService {
    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImp.class);
    private final  EntityManager entityManager;
    private final AccountService accountService;

    private final TransactionRepository transactionRepository;

    public TransactionResponseDto createTransaction(
            CreateTransactionDto payload,
            UUID userId
    ){
        try {
            // 1 Validate transaction payload

            // 2 Get current account reference!
            Account account = this.accountService.findAccountByUserId(userId);
            Account accountRef = entityManager.getReference(Account.class, account.getId());

            // 3 Create transaction
            Transaction transaction = new Transaction();
            transaction.setAccount(accountRef);
            transaction.setAmount(payload.amount());
            transaction.setDescription(payload.description());
            transaction.setType(payload.type());
            transaction.setReferenceAccountNumber(payload.referenceAccountNumber());
            transaction.setPostTransactionBalance(BigDecimal.valueOf(2.9000));

            // 4 Save the transaction
            this.transactionRepository.save(transaction);

            // Response
            TransactionDto response = new TransactionDto(
                    transaction.getId(),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getCurrency(),
                    transaction.getReferenceAccountNumber(),
                    transaction.getPostTransactionBalance(),
                    transaction.getDescription(),
                    transaction.getCreatedAt()
            );

            return new TransactionResponseDto(
                    response
            );
        } catch (Exception e) {
            this.logger.error("Error to create transaction { }", e);
            throw e;
        }
    }
}
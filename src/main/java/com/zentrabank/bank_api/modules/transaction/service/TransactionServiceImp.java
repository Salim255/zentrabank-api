package com.zentrabank.bank_api.modules.transaction.service;


import com.zentrabank.bank_api.modules.account.entity.Account.Account;
import com.zentrabank.bank_api.modules.account.service.AccountService;
import com.zentrabank.bank_api.modules.transaction.dto.CreateTransactionDto;
import com.zentrabank.bank_api.modules.transaction.dto.TransactionDto;
import com.zentrabank.bank_api.modules.transaction.dto.TransactionResponseDto;
import com.zentrabank.bank_api.modules.transaction.entity.Transaction;
import com.zentrabank.bank_api.modules.transaction.entity.TransactionType;
import com.zentrabank.bank_api.modules.transaction.repository.TransactionRepository;
import com.zentrabank.bank_api.modules.transaction.validation.TransactionValidator;
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
    private final TransactionValidator transactionValidator;

    private final TransactionRepository transactionRepository;

    public TransactionResponseDto createTransaction(
            CreateTransactionDto payload,
            UUID userId
    ){
        try {

            // 1 Get current account
            Account account = this.accountService.findAccountByUserId(userId);

            // 2. Validate transaction
            transactionValidator.validate(payload, account);

            // 3. Get reference account (only for transfers)
            Account referenceAccount = null;
            if (payload.type() == TransactionType.TRANSFER) {
                referenceAccount = accountService.findAccountByAccountNumber(payload.referenceAccountNumber());
            }

            // 4. Update balances
            // Get new balance
            BigDecimal newBalance = account.getBalance().subtract(payload.amount());

            // Update account balance
            account.setBalance(newBalance);

            // Save change
            accountService.saveAccountChange(account); // update main account

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
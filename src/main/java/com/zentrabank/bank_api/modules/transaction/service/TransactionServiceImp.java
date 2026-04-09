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
import jakarta.transaction.Transactional;
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



    public  TransactionResponseDto  transferOperation(
            CreateTransactionDto payload,
            UUID userId
    ){
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public TransactionResponseDto withdrawalOperation(
            CreateTransactionDto payload,
            UUID userId
    ){
        try {
            // 1 Get current account
            Account account = this.accountService.findAccountByUserId(userId);

            // 2 Lock accounts for update (pessimistic write)
            this.accountService.lockAccountForUpdate(account.getId());

            // 3 Validate transaction
            transactionValidator.validateWithdrawal(payload, account);

            // 4 Update balances
            // Get new balance
            BigDecimal newBalance = account.getBalance().subtract(payload.amount());

            // 5 Update account balance
            account.setBalance(newBalance);

            // 6 Save change
            accountService.saveAccountChange(account);

            // 7 Create transaction
            Transaction transaction = this.buildTransaction(payload, account, newBalance);

            // 8 Save the transaction
            this.transactionRepository.save(transaction);

            // 9 Response
            return new TransactionResponseDto(
                    this.toDto(transaction)
            );
        } catch (Exception e) {
            this.logger.error("Error during withdrawal transaction", e);
            throw e;
        }
    }

    @Transactional
    public TransactionResponseDto depositOperation(
            CreateTransactionDto payload,
            UUID userId
    ){
        try {
            // 1 Get current account
            Account account = this.accountService.findAccountByUserId(userId);

            // 2 Lock accounts for update (pessimistic write)
            this.accountService.lockAccountForUpdate(account.getId());

            // 3 Validate transaction
            transactionValidator.validateWithdrawal(payload, account);

            // 4 Update balances
            // Get new balance
            BigDecimal newBalance = account.getBalance().add(payload.amount());

            // 5 Update account balance
            account.setBalance(newBalance);

            // 6 Save change
            accountService.saveAccountChange(account);

            // 7 Create transaction
            Transaction transaction = this.buildTransaction(payload, account, newBalance);

            // 8 Save the transaction
            this.transactionRepository.save(transaction);

            // 9 Response
            return new TransactionResponseDto(
                    this.toDto(transaction)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public TransactionResponseDto createTransaction(
            CreateTransactionDto payload,
            UUID userId
    ){
        try {
            // 1 Get current account
            Account account = this.accountService.findAccountByUserId(userId);

            // 2 Lock account for update
            this.accountService.lockAccountForUpdate(account.getId());

            // 3. Validate transaction
            transactionValidator.validate(payload, account);

            // 4. Get reference account (only for transfers)
            Account referenceAccount = null;
            if (payload.type() == TransactionType.TRANSFER) {
                referenceAccount = accountService.findAccountByAccountNumber(payload.referenceAccountNumber());
            }

            // 5. Decide lock order to prevent deadlocks
            UUID fromAccountId = account.getId();
            UUID toAccountId = referenceAccount.getId();
            UUID firstAccountId = fromAccountId.compareTo(toAccountId) < 0 ? fromAccountId : toAccountId;
            UUID secondAccountId = fromAccountId.compareTo(toAccountId) < 0 ? toAccountId : fromAccountId;

            // 6 Lock accounts for update (pessimistic write)
            Account firstAccount = this.accountService.lockAccountForUpdate(firstAccountId);
            Account secondAccount = this.accountService.lockAccountForUpdate(secondAccountId);

            // 7 Map which one is sender and which is receiver
            Account sender = fromAccountId.equals(firstAccountId) ? firstAccount : secondAccount;
            Account receiver = toAccountId.equals(firstAccountId) ? firstAccount : secondAccount;

            // 5. Update balances
            // Get new balance
            BigDecimal newBalance = account.getBalance().subtract(payload.amount());

            // 6 Update account balance
            account.setBalance(newBalance);

            // 7 Save change
            accountService.saveAccountChange(account); // update main account

            // 8 Update receiver account
            if (referenceAccount != null) {
                BigDecimal refNewBalance = referenceAccount.getBalance().add(payload.amount());
                referenceAccount.setBalance(refNewBalance);
                accountService.saveAccountChange(referenceAccount);
            }

            // 9 Create transaction
            Transaction transaction = new Transaction();

            transaction.setAccount(referenceAccount);
            transaction.setAmount(payload.amount());
            transaction.setType(payload.type());
            transaction.setDescription(payload.description());
            transaction.setReferenceAccountNumber(payload.referenceAccountNumber());
            transaction.setPostTransactionBalance(newBalance);
            transaction.setCurrency(account.getCurrency());

            // 10 Save the transaction
            this.transactionRepository.save(transaction);

            // 11 Response
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

    private Transaction buildTransaction(
            CreateTransactionDto payload,
            Account account,
            BigDecimal newBalance
    ) {
        Transaction transaction = new Transaction();

        transaction.setAccount(account);
        transaction.setAmount(payload.amount());
        transaction.setType(payload.type());
        transaction.setDescription(payload.description());
        transaction.setReferenceAccountNumber(payload.referenceAccountNumber());
        transaction.setPostTransactionBalance(newBalance);
        transaction.setCurrency(account.getCurrency());

        return transaction;
    }

    private TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getReferenceAccountNumber(),
                transaction.getPostTransactionBalance(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );
    }
}
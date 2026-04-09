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
            // 1 Get current account
            Account senderAccount = this.accountService.findAccountByUserId(userId);

            // 2 Get receiver account
            Account receiverAccount = accountService
                    .findAccountByAccountNumber(payload.referenceAccountNumber());

            // 3. Validate transaction
            transactionValidator.validateTransfer(payload, senderAccount, receiverAccount);

            // 4. Decide lock order to prevent deadlocks
            UUID fromAccountId = senderAccount.getId();
            UUID toAccountId = receiverAccount.getId();


            // 5. Decide lock order to prevent deadlocks
            UUID firstAccountId = fromAccountId.compareTo(toAccountId) < 0 ? fromAccountId : toAccountId;
            UUID secondAccountId = fromAccountId.compareTo(toAccountId) < 0 ? toAccountId : fromAccountId;

            // 6 Lock accounts for update (pessimistic write)
            Account firstAccountLocked = this.accountService.lockAccountForUpdate(firstAccountId);
            Account secondAccountLocked = this.accountService.lockAccountForUpdate(secondAccountId);

            // 7 Map which one is sender and which is receiver
            // Map sender / receiver from locked entities
            Account sender = fromAccountId.equals(firstAccountId) ? firstAccountLocked : secondAccountLocked;
            Account receiver = toAccountId.equals(firstAccountId) ? firstAccountLocked :secondAccountLocked;

            // 5. Update balances
            // Get new balance
            BigDecimal amount = payload.amount();

            BigDecimal senderNewBalance = sender.getBalance().subtract(amount);
            BigDecimal receiverNewBalance = receiver.getBalance().add(amount);

            sender.setBalance(senderNewBalance);
            receiver.setBalance(receiverNewBalance);

            // 7 Save both accounts
            this.accountService.saveAccountChange(sender);
            this.accountService.saveAccountChange(receiver);


            // 9 Create transaction (sender side)
            Transaction transaction = buildTransaction(payload, sender, senderNewBalance);
            this.transactionRepository.save(transaction);

            // 10 Create transaction (receiver side)
            Transaction receivertransaction = buildTransaction(payload, sender, senderNewBalance);
            this.transactionRepository.save(receivertransaction);

            // 11 Return response
            return new TransactionResponseDto(toDto(transaction));
        } catch (Exception e) {
            this.logger.error("Error during transfer transaction", e);
            throw e;
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
            transactionValidator.validateDeposit(payload, account);

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
            this.logger.error("Error during deposit transaction", e);
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
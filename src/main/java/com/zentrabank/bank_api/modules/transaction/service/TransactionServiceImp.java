package com.zentrabank.bank_api.modules.transaction.service;


import com.zentrabank.bank_api.modules.account.entity.Account.Account;
import com.zentrabank.bank_api.modules.account.service.AccountService;
import com.zentrabank.bank_api.modules.transaction.dto.*;
import com.zentrabank.bank_api.modules.transaction.entity.Transaction;
import com.zentrabank.bank_api.modules.transaction.repository.TransactionRepository;
import com.zentrabank.bank_api.modules.transaction.validation.TransactionValidator;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionService {
    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImp.class);
    private final  EntityManager entityManager;
    private final TransactionMapper transactionMapper;
    private final AccountService accountService;
    private final TransactionValidator transactionValidator;

    private final TransactionRepository transactionRepository;

    @Override
    public GetTransactionsResponseDto getTransactionsForAccount(UUID userId, int page, int size){
        try {
            // 1 Fetch account by userId =
            Account account = this.accountService.findAccountByUserId(userId);
            List<TransactionDto> transactions = this.transactionRepository
                    .findByAccountAccountNumber(account.getAccountNumber())
                    .stream()
                    .map(transactionMapper::toDto)
                    .toList();

            return new GetTransactionsResponseDto(transactions);
        } catch (Exception e) {
            this.logger.error("Error in getting transactions", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public  TransactionResponseDto  transferOperation(
            TransferDto payload,
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

            // 4 Lock accounts safely
            LockedAccountsDto locked = lockAccounts(senderAccount, receiverAccount);


           // 5 Blocked accounts
            Account sender = locked.getSender();
            Account receiver = locked.getReceiver();

            // 6 Update balances
            // Get new balance
            BigDecimal amount = payload.amount();

            BigDecimal senderNewBalance = sender.getBalance().subtract(amount);
            BigDecimal receiverNewBalance = receiver.getBalance().add(amount);

            // Update balance
            sender.setBalance(senderNewBalance);
            receiver.setBalance(receiverNewBalance);

            // 7 Save both accounts
            this.accountService.saveAccountChange(sender);
            this.accountService.saveAccountChange(receiver);

            // 9 Create transaction (sender side)
            // 10 Create transaction (receiver side)
            Transaction senderTransaction = buildTransfer(payload, sender, senderNewBalance);
            Transaction receivertransaction = buildTransfer(payload, receiver, receiverNewBalance);

            this.transactionRepository.save(senderTransaction);
            this.transactionRepository.save(receivertransaction);

            // 11 Return response
            return new TransactionResponseDto(toDto(senderTransaction));
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

    private LockedAccountsDto lockAccounts(Account senderAccount, Account receiverAccount) {

        // Extract the unique IDs of both accounts
        // We use IDs because they are comparable and stable identifiers
        UUID fromAccountId = senderAccount.getId();
        UUID toAccountId = receiverAccount.getId();

        // Decide a consistent locking order to prevent deadlocks
        // Why? If two transactions try to lock the same accounts in different orders,
        // they can block each other forever (deadlock)
        // Solution: always lock in the SAME order based on ID comparison
        UUID firstAccountId = fromAccountId.compareTo(toAccountId) < 0 ? fromAccountId : toAccountId;
        UUID secondAccountId = fromAccountId.compareTo(toAccountId) < 0 ? toAccountId : fromAccountId;

        // Lock the first account in the decided order
        // This ensures that no other transaction can modify it until we finish
        Account firstAccountLocked = this.accountService.lockAccountForUpdate(firstAccountId);

        // Lock the second account AFTER the first one
        // Because all transactions follow the same order,
        // deadlocks are avoided
        Account secondAccountLocked = this.accountService.lockAccountForUpdate(secondAccountId);

        // Now we need to map back:
        // Which locked account is the sender?
        // If sender's ID matches firstId → sender = first, else sender = second
        Account sender = fromAccountId.equals(firstAccountId) ? firstAccountLocked : secondAccountLocked;

        // Same logic for receiver:
        // We check which locked account corresponds to the receiver
        Account receiver = toAccountId.equals(firstAccountId) ? firstAccountLocked :secondAccountLocked;

        // Return both locked accounts wrapped in a helper object
        // This makes the method clean and avoids returning multiple values manually
        return new LockedAccountsDto(sender, receiver);
    }

    private Transaction buildTransfer(
            TransferDto payload,
            Account account,
            BigDecimal newBalance
    ) {
        Transaction transaction = new Transaction();

        // transaction.setSender(account);
        transaction.setAmount(payload.amount());
        transaction.setType(payload.type());
        //transaction.setDescription(payload.description());
        transaction.setPostTransactionBalance(newBalance);
        //transaction.setCurrency(account.getCurrency());

        return transaction;
    }

    private Transaction buildTransaction(
            CreateTransactionDto payload,
            Account account,
            BigDecimal newBalance
    ) {
        Transaction transaction = new Transaction();

        //transaction.setSender(account);
        transaction.setAmount(payload.amount());
        transaction.setType(payload.type());
        //transaction.setDescription(payload.description());
        transaction.setPostTransactionBalance(newBalance);
        //transaction.setCurrency(account.getCurrency());

        return transaction;
    }

    private TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getTransfer().getDescription(),
                transaction.getPostTransactionBalance(),
                transaction.getTransfer().getExternalRecipientName(),
                transaction.getTransfer().getFromAccount().getIban(),
                transaction.getTransfer().getStatus(),
                transaction.getCreatedAt()
        );
    }
}
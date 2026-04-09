package com.zentrabank.bank_api.modules.transaction.validation;

import com.zentrabank.bank_api.exceptions.BadRequestException;
import com.zentrabank.bank_api.exceptions.NotFoundException;
import com.zentrabank.bank_api.modules.account.entity.Account.Account;
import com.zentrabank.bank_api.modules.account.service.AccountService;
import com.zentrabank.bank_api.modules.transaction.dto.CreateTransactionDto;
import com.zentrabank.bank_api.modules.transaction.entity.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionValidator {
    private final AccountService accountService;

    public  void validateWithdrawal(CreateTransactionDto payload, Account account){

        if (payload.amount() == null || payload.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be greater than zero");
        }

        if (account == null) {
            throw new NotFoundException("Account not found");
        }

        if (account.getBalance().compareTo(payload.amount()) < 0) {
            throw new BadRequestException("Insufficient balance");
        }

        // if (!account.getCurrency().equals(payload.currency())) {
           // throw new BadRequestException("Currency mismatch");
        //}

        // Optional: business rules
        if (payload.amount().compareTo(new BigDecimal("10000")) > 0) {
            throw new BadRequestException("Withdrawal limit exceeded");
        }
    }

    public void validate(CreateTransactionDto payload, Account account){
        // 1. Amount
        if (payload.amount() == null || payload.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        // 2. Reference account for TRANSFER
        if (payload.type() == TransactionType.TRANSFER){
            if (payload.referenceAccountNumber() == null || payload.referenceAccountNumber().isBlank()) {
                throw new IllegalArgumentException("Reference account is required for transfers");
            }

            if (payload.referenceAccountNumber().equals(account.getAccountNumber())) {
                throw new IllegalArgumentException("Cannot transfer to the same account");
            }

            // Check if reference account exists
            accountService.findAccountByAccountNumber(payload.referenceAccountNumber());
        }

        // 3. Check sufficient balance for DEBIT/TRANSFER
        if ((payload.type() == TransactionType.WITHDRAWAL || payload.type() == TransactionType.TRANSFER)
                && account.getBalance().compareTo(payload.amount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
    }
}
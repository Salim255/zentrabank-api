package com.zentrabank.bank_api.modules.account.service;

import com.zentrabank.bank_api.exceptions.NotFoundException;
import com.zentrabank.bank_api.modules.account.dto.AccountDto;
import com.zentrabank.bank_api.modules.account.dto.CreateAccountDto;
import com.zentrabank.bank_api.modules.account.dto.CreateAccountResponseDto;
import com.zentrabank.bank_api.modules.account.entity.Account.Account;
import com.zentrabank.bank_api.modules.account.repository.AccountRepository;
import com.zentrabank.bank_api.modules.user.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class AccountServiceImp implements AccountService {
    private  final  EntityManager entityManager;
    private  final  AccountRepository accountRepository;
    private  final Logger logger = LoggerFactory.getLogger(AccountServiceImp.class);

    @Override
    public void saveAccountChange(Account account) {
        try {
            this.accountRepository.save(account);
        } catch (Exception e) {
            this.logger.error("Error in update account { }",e);
            throw e;
        }
    }

    @Override
    public Account findAccountByAccountNumber(String accountNumber){
        try {
            return this.accountRepository
                    .findAccountByAccountNumber(accountNumber)
                    .orElseThrow(() -> new NotFoundException("Account not found for given account number"));
        } catch (Exception ex) {
            this.logger.error("Error to find account by accountId { }", ex);
            throw ex;
        }
    }

    @Override
    public Account findAccountByUserId(UUID userId){
        try {
            return this.accountRepository
                    .findByUserId(userId)
                    .orElseThrow(() -> new NotFoundException("No account found for this userId"));
        } catch (Exception e) {
            this.logger.error("Error to get account by userId ");
            throw e;
        }
    }

    @Override
    public CreateAccountResponseDto createAccount(CreateAccountDto payload, UUID userId) {
        try {

            // 1 Get user reference
            User userRef = this.entityManager.getReference(User.class, userId);

            // 2 Generate account number
            String accountNumber = accountNumberGenerator();

            // 2 Built account
            Account newAccount = new Account();
            newAccount.setType(payload.accountType());
            newAccount.setUser(userRef);
            newAccount.setAccountNumber(accountNumber);

            // 3 Create account
            this.accountRepository.save(newAccount);

            return new CreateAccountResponseDto(
                    new AccountDto(
                            newAccount.getId(),
                            newAccount.getAccountNumber(),
                            newAccount.getBalance(),
                            newAccount.getType(),
                            newAccount.getStatus(),
                            newAccount.getCurrency(),
                            newAccount.getOverdraftLimit()
                    )
            );
        } catch (Exception e) {
            this.logger.error("Error creating account { }", e);
            throw e;
        }
    }

    @Override
    public  String updateAccount(){
        return  "";
    }

    @Override
    public  String blockAccount(){
        return  "";
    }

    @Override
    public  String deleteAccount(){
        return  "";
    }

    private String accountNumberGenerator(){
        String accountNumber;
        do {
            // Generate 9-digit number (100000000 → 999999999)
            int number = ThreadLocalRandom.current().nextInt(100_000_000, 1_000_000_000);
            accountNumber = String.valueOf(number);
        } while (this.accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }
}
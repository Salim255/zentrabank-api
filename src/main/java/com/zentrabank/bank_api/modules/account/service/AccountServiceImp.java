package com.zentrabank.bank_api.modules.account.service;

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

@RequiredArgsConstructor
@Service
public class AccountServiceImp implements AccountService {
    private  final  EntityManager entityManager;
    private  final  AccountRepository accountRepository;
    private  final Logger logger = LoggerFactory.getLogger(AccountServiceImp.class);

    @Override
    public CreateAccountResponseDto createAccount(CreateAccountDto payload) {
        try {

            // 1 Get user refrence
            User user = this.entityManager.getReference(User.class, payload.userId());

            // 2 Generate account number
            String accountNumber = accountNumberGenerator();

            // 2 Built account
            Account newAccount = Account.builder()
                    .user(user)
                    .type(payload.accountType())
                    .accountNumber(accountNumber)
                    .build();
            // 3 Create account
            this.accountRepository.save(newAccount);


        } catch (Exception e) {
            throw new RuntimeException(e);
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

    private static  String accountNumberGenerator(){
        return "123";
    }
}
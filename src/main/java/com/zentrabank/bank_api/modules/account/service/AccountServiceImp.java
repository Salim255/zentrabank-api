package com.zentrabank.bank_api.modules.account.service;

import com.zentrabank.bank_api.modules.account.dto.CreateAccountDto;
import com.zentrabank.bank_api.modules.account.dto.CreateAccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountServiceImp implements AccountService {
    private  final Logger logger = LoggerFactory.getLogger(AccountServiceImp.class);

    @Override
    public CreateAccountResponseDto createAccount(CreateAccountDto payload) {
        try {

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

}
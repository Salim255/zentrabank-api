package com.zentrabank.bank_api.modules.account.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @PostMapping()
    public  String createAccount(){
        return "Hello from create account";
    }

    public  String updateAccount(){
        return  "hello from update account";
    }

    public  String blockAccount(){
        return  "Hello from block account";
    }

    public  String deleteAccount(){
        return  "Hello from delete account";
    }

}
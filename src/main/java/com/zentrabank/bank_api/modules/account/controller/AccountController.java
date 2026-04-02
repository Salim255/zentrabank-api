package com.zentrabank.bank_api.modules.account.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {
    public  String createAccount(){
        return "Hello from create account";
    }
}
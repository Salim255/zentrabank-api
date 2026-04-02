package com.zentrabank.bank_api.modules.transaction.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public  class TransactionController {
    public String createTransaction(){
        return "hello from create transaction";
    }
}
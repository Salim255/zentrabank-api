package com.zentrabank.bank_api.modules.transaction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public  class TransactionController {

    @PostMapping()
    public String createTransaction(){
        return "hello from create transaction";
    }
}
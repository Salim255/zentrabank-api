package com.zentrabank.bank_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class  AccountFrozenException extends ResponseStatusException {
    public AccountFrozenException(String message){
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
package com.zentrabank.bank_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidIbanException extends ResponseStatusException {
    public InvalidIbanException(String message){
        super(HttpStatus.BAD_REQUEST, message);
    }
}
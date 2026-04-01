package com.zentrabank.bank_api.exceptions;

import com.zentrabank.bank_api.config.BankApiConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;

// We use @ControllerAdvice to handle
// exceptions thrown by any controller in one central place.
@ControllerAdvice
public class GlobalException {
    private final BankApiConfigProperties config;
    private final Logger logger = LoggerFactory.getLogger(GlobalException.class);

    public GlobalException(BankApiConfigProperties config){
        this.config = config;
    }
}
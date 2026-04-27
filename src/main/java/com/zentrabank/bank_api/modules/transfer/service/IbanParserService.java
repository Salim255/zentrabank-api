package com.zentrabank.bank_api.modules.transfer.service;

import org.springframework.stereotype.Service;

@Service
public class IbanParserService {

    private static final String BANK_CODE = "12345"; // your bank code

    public boolean isInternalIban(String iban) {
        if (iban == null || iban.length() < 14) return false;
        return iban.substring(4, 9).equals(BANK_CODE);
    }

    public String extractAccountNumber(String iban) {
        // FRkk BBBB BGGG GGCC CCCC CCCC CKK
        // account number = positions 14 to 25 (11 digits)
        return iban.substring(14, 25);
    }
}

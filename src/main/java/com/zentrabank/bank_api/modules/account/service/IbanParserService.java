package com.zentrabank.bank_api.modules.account.service;

import com.zentrabank.bank_api.common.constants.BankConstants;
import org.springframework.stereotype.Service;

@Service
public class IbanParserService {

    // ---------------------------------------------------------
    // CHECK IF IBAN BELONGS TO OUR BANK
    // ---------------------------------------------------------
    public boolean isInternalIban(String iban) {
        if (iban == null || iban.length() < 27) return false;

        // FRkk + BANK_CODE
        String bankCodeInIban = iban.substring(4, 9);
        return bankCodeInIban.equals(BankConstants.BANK_CODE);
    }

    // ---------------------------------------------------------
    // EXTRACT INTERNAL ACCOUNT NUMBER (11 digits)
    // ---------------------------------------------------------
    public String extractAccountNumber(String iban) {
        if (iban == null || iban.length() < 27) {
            throw new IllegalArgumentException("Invalid IBAN format");
        }

        // FRkk BBBBB GGGGG ACCOUNTTTTTTTT CC
        return iban.substring(14, 25); // 11 digits
    }

    // ---------------------------------------------------------
    // EXTRACT NATIONAL CHECK (2 digits)
    // ---------------------------------------------------------
    public String extractNationalCheck(String iban) {
        if (iban == null || iban.length() < 27) {
            throw new IllegalArgumentException("Invalid IBAN format");
        }

        return iban.substring(25, 27);
    }
}

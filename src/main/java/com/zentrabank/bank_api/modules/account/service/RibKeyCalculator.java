package com.zentrabank.bank_api.modules.account.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class RibKeyCalculator {
    public String computeRibKey(String bankCode, String branchCode, String accountNumber) {

        // Step 1: Convert letters in account number (A=1, B=2, ..., Z=26)
        StringBuilder converted = new StringBuilder();

        for (char c : accountNumber.toUpperCase().toCharArray()) {
            if (Character.isLetter(c)) {
                // A = 1 → Z = 26
                converted.append(c - 'A' + 1);
            } else {
                converted.append(c);
            }
        }

        // Step 2: Build the numeric string
        String full = bankCode + branchCode + converted;

        // Step 3: Compute RIB key using MOD 97
        // FIX: Use BigInteger (BBAN can exceed 20 digits)
        BigInteger num = new BigInteger(full);

        // Compute RIB key using official French formula:
        // key = 97 - (BBAN mod 97)
        int key = 97 - num.mod(BigInteger.valueOf(97)).intValue();
        // Step 4: Always return 2 digits
        return String.format("%02d", key);
    }
}
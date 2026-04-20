package com.zentrabank.bank_api.modules.account.service;

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
        long num = Long.parseLong(full);
        long key = 97 - (num % 97);

        // Step 4: Always return 2 digits
        return String.format("%02d", key);
    }
}
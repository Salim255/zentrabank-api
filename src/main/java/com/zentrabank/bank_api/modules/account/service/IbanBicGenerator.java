package com.zentrabank.bank_api.modules.account.service;

import java.security.SecureRandom;

public class IbanBicGenerator {
    // SecureRandom is cryptographically strong → required for banking identifiers
    private static final SecureRandom random = new SecureRandom();

    // ------------------------------------------------------------
    // GENERATE FRENCH IBAN (FR)
    // ------------------------------------------------------------
    public String generateFrenchIban(){
        // Bank code: 5 digits (FR standard)
        // Why? French IBAN structure requires 5-digit bank code.
        String bankCode = randomDigits(5);

        // Branch code: 5 digits (FR standard)
        // Why? Identifies the branch of the bank.
        String branchCode = randomDigits(5);

        // Account number: 11 digits (FR standard)
        // Why? French BBAN requires 11-digit account number.
        String accountNumber = randomDigits(11);

        // National check digits: 2 digits
        // Why? French BBAN ends with 2-digit national checksum.
        String nationalCheck = randomDigits(2);

        // BBAN = BankCode + BranchCode + AccountNumber + NationalCheck
        // Why? IBAN is built from BBAN + country + checksum.
        String bban = bankCode + branchCode + accountNumber + nationalCheck;

        // Compute IBAN check digits using MOD-97 algorithm
        // Why? Required by ISO 13616. Without this, IBAN is invalid.
        String checkDigits = computeIbanCheckDigits("FR", bban);

        // Final IBAN = CountryCode + CheckDigits + BBAN
        return "FR" + checkDigits + bban;
    }

    // ------------------------------------------------------------
    // GENERATE BIC (SWIFT)
    // ------------------------------------------------------------
    public String generateBic(){
        // Bank code: 4 letters
        // Why? SWIFT standard requires 4-letter bank identifier.
        String bankCode = randomLetters(4);

        // Country code: FR (France)
        // Why? Your bank is French → BIC must reflect country.
        String countryCode = "FR";

        // Location code: 2 alphanumeric characters
        // Why? Identifies region or city.
        String locationCode = randomLettersOrDigits(2);

        // Branch code: XXX (default)
        // Why? XXX = primary office. Optional but recommended.
        String branchCode = "XXX";

        // Final BIC = AAAA BB CC DDD
        return bankCode + countryCode + locationCode + branchCode;
    }
}
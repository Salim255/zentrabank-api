package com.zentrabank.bank_api.modules.account.service;

import com.zentrabank.bank_api.common.constants.BankConstants;

import java.math.BigInteger;
import java.security.SecureRandom;

public class IbanBicGenerator {
    // SecureRandom is cryptographically strong → required for banking identifiers
    private static final SecureRandom random = new SecureRandom();

    // ------------------------------------------------------------
    // GENERATE FRENCH IBAN (FR)
    // ------------------------------------------------------------
    public String generateFrenchIban(String accountNumber, String nationalCheck){
        // BBAN = BankCode + BranchCode + AccountNumber + NationalCheck
        // Why? IBAN is built from BBAN + country + checksum.
        // Build BBAN using constants + dynamic values
        String bban = BankConstants.BANK_CODE
                + BankConstants.BRANCH_CODE
                + accountNumber
                + nationalCheck;

        // Compute IBAN check digits using MOD-97 algorithm
        // Why? Required by ISO 13616. Without this, IBAN is invalid.
        String checkDigits = computeIbanCheckDigits(bban);

        // Final IBAN = CountryCode + CheckDigits + BBAN
        return BankConstants.COUNTRY_CODE + checkDigits + bban;
    }

    // ------------------------------------------------------------
    // GENERATE BIC (SWIFT)
    // ------------------------------------------------------------
    public String generateBic(){
        // Bank code: 4 letters
        // Why? SWIFT standard requires 4-letter bank identifier.
        String bankCode = randomLetters();

        // Country code: FR (France)
        // Why? Your bank is French → BIC must reflect country.
        String countryCode = "FR";

        // Location code: 2 alphanumeric characters
        // Why? Identifies region or city.
        String locationCode = randomLettersOrDigits();

        // Branch code: XXX (default)
        // Why? XXX = primary office. Optional but recommended.
        String branchCode = "XXX";

        // Final BIC = AAAA BB CC DDD
        return bankCode + countryCode + locationCode + branchCode;
    }

    public String getBic() {
        return BankConstants.BIC;
    }
    // ------------------------------------------------------------
    // IBAN CHECK DIGIT CALCULATION (MOD-97)
    // ------------------------------------------------------------
    private String computeIbanCheckDigits(String bban){
        String rearranged = bban + BankConstants.COUNTRY_CODE + "00";

        StringBuilder numeric = new StringBuilder();
        for (char c : rearranged.toCharArray()) {
            if (Character.isLetter(c)) {
                numeric.append((int) c - 55);
            } else {
                numeric.append(c);
            }
        }

        BigInteger num = new BigInteger(numeric.toString());
        int mod97 = num.mod(BigInteger.valueOf(97)).intValue();

        int checkDigits = 98 - mod97;
        return String.format("%02d", checkDigits);
    }


    // ------------------------------------------------------------
    // HELPER: RANDOM LETTERS
    // ------------------------------------------------------------
    private String randomLetters() {
        // Generates uppercase letters A–Z
        // Why? BIC bank code must be letters only.
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    // ------------------------------------------------------------
    // HELPER: RANDOM LETTERS OR DIGITS
    // ------------------------------------------------------------
    private String randomLettersOrDigits() {
        // Used for BIC location code (letters or digits allowed)
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
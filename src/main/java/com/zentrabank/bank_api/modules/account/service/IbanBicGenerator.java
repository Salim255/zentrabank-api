package com.zentrabank.bank_api.modules.account.service;

import java.math.BigInteger;
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

    // ------------------------------------------------------------
    // IBAN CHECK DIGIT CALCULATION (MOD-97)
    // ------------------------------------------------------------
    private String computeIbanCheckDigits(String countryCode, String bban){
        // Rearrange: BBAN + CountryCode + "00"
        // Why? ISO 13616 requires moving country code to end with "00" placeholder.
        String rearranged = bban + countryCode + "00";

        // Convert letters to numbers: A=10, B=11, ..., Z=35
        // Why? MOD-97 works only on numeric strings.
        StringBuilder numeric = new StringBuilder();

        for (char c : rearranged.toCharArray()) {
            if (Character.isLetter(c)) {
                // Convert letter to number by subtracting 55 (A=65 ASCII → 65-55=10)
                numeric.append((int) c - 55);
            } else {
                // Keep digits as-is
                numeric.append(c);
            }
        }

        // Convert numeric string to BigInteger
        // Why? IBAN numbers exceed long range → BigInteger required.
        BigInteger num = new BigInteger(numeric.toString());

        // Compute MOD-97
        // Why? IBAN checksum = 98 - (numeric % 97)
        int mod97 = num.mod(BigInteger.valueOf(97)).intValue();

        // Final check digits
        int checkDigits = 98 - mod97;

        // Always 2 digits (pad with zero if needed)
        return String.format("%02d", checkDigits);
    }

    // ------------------------------------------------------------
    // HELPER: RANDOM DIGITS
    // ------------------------------------------------------------
    private String randomDigits(int length) {
        // Generates a numeric string of given length
        // Why? Used for bank code, branch code, account number, etc.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 0–9
        }
        return sb.toString();
    }
}
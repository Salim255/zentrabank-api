package com.zentrabank.bank_api.common.constants;

/**
 * Centralized constants for bank identity.
 * These values are used for IBAN generation and validation.
 */
public class BankConstants {

    private BankConstants() {} // Prevent instantiation

    // ISO country code for France
    public static final String COUNTRY_CODE = "FR";

    // 5-digit French bank code (Code Banque)
    // Choose your official bank code here
    // Bank code: 5 digits (FR standard)
    // Why? French IBAN structure requires 5-digit bank code.
    public static final String BANK_CODE = "12345";

    // 5-digit branch code (Code Guichet)
    // Online banks typically use a single branch code
    // Branch code: 5 digits (FR standard)
    // Why? Identifies the branch of the bank.
    public static final String BRANCH_CODE = "00001";

    // Bank
    //Identifier
    //Code
    // BIC (SWIFT code) for your bank
    // Format: AAAA BB CC DDD
    // NO — not assigned by SWIFT (because your bank is fictional)
    public static final String BIC = "ZENTFRPPXXX";
}
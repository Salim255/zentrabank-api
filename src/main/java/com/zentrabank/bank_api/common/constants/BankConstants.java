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
    public static final String BANK_CODE = "12345";

    // 5-digit branch code (Code Guichet)
    // Online banks typically use a single branch code
    public static final String BRANCH_CODE = "00001";

    // BIC (SWIFT code) for your bank
    // Format: AAAA BB CC DDD
    public static final String BIC = "ZENTFRPPXXX";
}
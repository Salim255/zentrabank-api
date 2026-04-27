package com.zentrabank.bank_api.modules.transaction.entity;

public enum TransactionType {
    DEPOSIT,            // +amount
    WITHDRAWAL,         // -amount
    TRANSFER_DEBIT,     // -amount (sender)
    TRANSFER_CREDIT     // +amount (receiver)
}
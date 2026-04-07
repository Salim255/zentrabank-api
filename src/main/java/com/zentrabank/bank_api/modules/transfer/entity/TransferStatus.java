package com.zentrabank.bank_api.modules.transfer.entity;

public  enum TransferStatus {
    PENDING,     // Created but not completed yet
    COMPLETED,   // Successfully executed
    FAILED       // Failed (insufficient funds, etc.)
}
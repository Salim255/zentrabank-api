package com.zentrabank.bank_api.modules.auditlogs.entity;

public enum ActionType {
    LOGIN,
    LOGOUT,
    REGISTER,
    TRANSFER,
    DEPOSIT,
    WITHDRAWAL,
    REFRESH_TOKEN,
    FAILED_LOGIN
}
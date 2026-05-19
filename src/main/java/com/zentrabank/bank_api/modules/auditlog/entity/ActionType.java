package com.zentrabank.bank_api.modules.auditlog.entity;

public enum ActionType {
    LOGIN,
    LOGOUT,
    CREATE_USER,
    UPDATE_USER,
    DELETE_USER,
    SYSTEM_ERROR,
    API_CALL,
    TRANSFER,
    DEPOSIT,
    WITHDRAWAL,
    REFRESH_TOKEN,
    FAILED_LOGIN
}
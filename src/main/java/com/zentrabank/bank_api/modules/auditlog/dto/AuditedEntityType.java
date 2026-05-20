package com.zentrabank.bank_api.modules.auditlog.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "List of auditable entity types.")
public enum AuditedEntityType {
    USER,
    PROFILE,
    ACCOUNT,
    TRANSACTION,
    CARD,
    TRANSFER,
    BENEFICIARY,
}
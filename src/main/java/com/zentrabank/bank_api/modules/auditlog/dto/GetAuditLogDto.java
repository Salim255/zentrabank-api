package com.zentrabank.bank_api.modules.auditlog.dto;

import java.util.UUID;

public  record GetAuditLogDto(
        String entityName,
        UUID userId
) { }
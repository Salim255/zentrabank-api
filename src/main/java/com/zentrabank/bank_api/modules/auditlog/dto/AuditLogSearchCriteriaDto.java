package com.zentrabank.bank_api.modules.auditlog.dto;

import java.time.LocalDateTime;

public  record AuditLogSearchCriteriaDto(
        String entityName,
        String entityId,
        Long userId,
        String action,
        LocalDateTime from,
        LocalDateTime to
) {
}
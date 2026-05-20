package com.zentrabank.bank_api.modules.auditlog.service;

import com.zentrabank.bank_api.modules.auditlog.dto.AuditLogSearchCriteriaDto;
import com.zentrabank.bank_api.modules.auditlog.dto.CreateAuditLogDto;
import com.zentrabank.bank_api.modules.auditlog.entity.AuditLog;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

public interface AuditLogService {
    public AuditLog createAuditLog(CreateAuditLogDto log, UUID userId);
    public List<AuditLog>  getLogsForEntity(String entityName, UUID entityId);
    public List<AuditLog> getLogsForUser(UUID userId);
    public List<AuditLog> search(AuditLogSearchCriteriaDto criteria, Pageable pageable);
}
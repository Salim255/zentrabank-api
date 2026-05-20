package com.zentrabank.bank_api.modules.auditlog.service;

import com.zentrabank.bank_api.modules.auditlog.dto.AuditLogSearchCriteriaDto;
import com.zentrabank.bank_api.modules.auditlog.dto.CreateAuditLogDto;
import com.zentrabank.bank_api.modules.auditlog.entity.AuditLog;

import java.awt.print.Pageable;
import java.util.List;

public interface AuditLogService {
    public AuditLog createAuditLog(CreateAuditLogDto log);
    public List<AuditLog>  getLogsForEntity();
    public List<AuditLog> getLogsForUser();
    public List<AuditLog> search(AuditLogSearchCriteriaDto criteria, Pageable pageable);
}
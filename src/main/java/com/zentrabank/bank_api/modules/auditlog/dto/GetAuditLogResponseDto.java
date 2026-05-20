package com.zentrabank.bank_api.modules.auditlog.dto;

import com.zentrabank.bank_api.modules.auditlog.entity.AuditLog;

import java.util.List;

public  record GetAuditLogResponseDto(List<AuditLog> logs) { }
package com.zentrabank.bank_api.modules.auditlog.dto;

import com.zentrabank.bank_api.modules.account.dto.AccountDto;
import com.zentrabank.bank_api.modules.auditlog.entity.AuditLog;

public  record CreateAuditLogResponseDto(AuditLog log) { }

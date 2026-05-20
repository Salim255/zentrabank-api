package com.zentrabank.bank_api.modules.auditlog.controller;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.modules.auditlog.dto.CreateAuditLogDto;
import com.zentrabank.bank_api.modules.auditlog.dto.CreateAuditLogResponseDto;
import com.zentrabank.bank_api.modules.auditlog.service.AuditLogService;
import com.zentrabank.bank_api.modules.transfer.dto.GetTransferResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/audit-logs")
public class AuditLogController {
    private  final AuditLogService auditLogService;

    @PostMapping("")
    public ApiResponseDto<CreateAuditLogResponseDto> createAudit(
            @Valid @RequestBody CreateAuditLogDto log,
            Authentication auth
    ) {
        UUID userId = (UUID) auth.getPrincipal();
        this.auditLogService.createAuditLog(log, userId);
        return ApiResponseDto.success(new CreateAuditLogResponseDto(this.auditLogService.createAuditLog(log, userId)));
    }

    @GetMapping("")
    public  ApiResponseDto<> getLogsForEntity(
            @Valid @RequestBody CreateAuditLogDto log,
            Authentication auth
    ){
        UUID userId = (UUID) auth.getPrincipal();
        this.auditLogService.getLogsForEntity("transfer")
    }
}
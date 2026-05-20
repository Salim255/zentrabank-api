package com.zentrabank.bank_api.modules.auditlog.controller;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.modules.auditlog.dto.*;
import com.zentrabank.bank_api.modules.auditlog.service.AuditLogService;
import com.zentrabank.bank_api.modules.transfer.dto.GetTransferResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/audit-logs")
public class AuditLogController {
    private  final AuditLogService auditLogService;

    @PostMapping
    public ApiResponseDto<CreateAuditLogResponseDto> createAudit(
            @Valid @RequestBody CreateAuditLogDto log,
            Authentication auth
    ) {
        UUID userId = (UUID) auth.getPrincipal();
        this.auditLogService.createAuditLog(log, userId);
        return ApiResponseDto.success(new CreateAuditLogResponseDto(this.auditLogService.createAuditLog(log, userId)));
    }

    @GetMapping("entities")
    public  ApiResponseDto<GetAuditLogResponseDto> getLogsForEntity(
            @Valid @RequestBody GetAuditLogDto payload,
            Authentication auth
    ){
        UUID userId = (UUID) auth.getPrincipal();
        return  ApiResponseDto.success(new GetAuditLogResponseDto(
                this.auditLogService.getLogsForEntity(payload.entityName(), userId)
        ));
    }

    @GetMapping("users")
    public  ApiResponseDto<GetAuditLogResponseDto> getLogsForUser(
            @Valid @RequestBody GetAuditLogDto payload,
            Authentication auth
    ){
        UUID userId = (UUID) auth.getPrincipal();
        return  ApiResponseDto.success(new GetAuditLogResponseDto(
                this.auditLogService.getLogsForUser(userId)
        ));
    }

    @GetMapping("search")
    public  ApiResponseDto<GetAuditLogResponseDto> searchLogs(
            @Valid @RequestBody AuditLogSearchCriteriaDto payload,
            Pageable pageable,
            Authentication auth
    ){
        UUID userId = (UUID) auth.getPrincipal();
        return  ApiResponseDto.success(new GetAuditLogResponseDto(
                this.auditLogService.search(payload, pageable)
        ));
    }
}
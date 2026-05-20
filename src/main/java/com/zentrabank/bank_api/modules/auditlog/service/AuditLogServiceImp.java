package com.zentrabank.bank_api.modules.auditlog.service;

import com.zentrabank.bank_api.exceptions.NotFoundException;
import com.zentrabank.bank_api.modules.auditlog.dto.AuditLogSearchCriteriaDto;
import com.zentrabank.bank_api.modules.auditlog.dto.CreateAuditLogDto;
import com.zentrabank.bank_api.modules.auditlog.entity.AuditLog;
import com.zentrabank.bank_api.modules.auditlog.repository.AuditLogRepository;
import com.zentrabank.bank_api.modules.user.entity.User;
import com.zentrabank.bank_api.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuditLogServiceImp implements AuditLogService {
    private final UserService userService;
    private final AuditLogRepository auditLogRepository;

    @Override
    public AuditLog createAuditLog(CreateAuditLogDto log, UUID userId){
        try {
            User user = this.userService
                    .getUser(userId).orElseThrow(() -> new NotFoundException("User not found"));
            AuditLog createdLog = new AuditLog();
            createdLog.setUser(user);
            this.auditLogRepository.save(createdLog);
            return createdLog;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    @Override
    public List<AuditLog> getLogsForEntity(String entityName, UUID entityId){
        try {
            return this.auditLogRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };
    @Override
    public List<AuditLog> getLogsForUser(UUID userId){
        try {
            return this.auditLogRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };
    @Override
    public List<AuditLog> search(AuditLogSearchCriteriaDto criteria, Pageable pageable){
        try {
            return this.auditLogRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };
}

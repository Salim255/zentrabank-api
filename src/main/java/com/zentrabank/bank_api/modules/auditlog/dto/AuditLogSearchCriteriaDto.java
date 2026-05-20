package com.zentrabank.bank_api.modules.auditlog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "Search filters for querying audit logs.")
public  record AuditLogSearchCriteriaDto(

        @Schema(
                description = "Name of the audited entity (e.g., 'User', 'Account', 'Transaction').",
                example = "User"
        )
        @Size(max = 100, message = "Entity name must not exceed 100 characters.")
        String entityName,

        @Schema(
                description = "Identifier of the audited entity. Supports UUID, Long, or composite keys.",
                example = "42"
        )
        @Size(max = 200, message = "Entity ID must not exceed 200 characters.")
        String entityId,

        @Schema(
                description = "ID of the user who performed the action.",
                example = "7"
        )
        Long userId,

        @Schema(
                description = "Action type to filter by (e.g., 'LOGIN', 'TRANSFER', 'UPDATE_PROFILE').",
                example = "UPDATE_PROFILE"
        )
        @Size(max = 50, message = "Action must not exceed 50 characters.")
        String action,

        @Schema(
                description = "Start of the date range for filtering logs.",
                example = "2025-01-01T00:00:00"
        )
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime from,

        @Schema(
                description = "End of the date range for filtering logs.",
                example = "2025-01-31T23:59:59"
        )
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime to
) {
}
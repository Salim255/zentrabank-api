package com.zentrabank.bank_api.modules.auditlog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload used to create an audit log entry.")
public record CreateAuditLogDto(
        @Schema(
                description = "Name of the entity being audited (e.g., 'User', 'Order', 'Product').",
                example = "User"
        )
        @NotBlank(message = "Entity name is required.")
        @Size(max = 100, message = "Entity name must not exceed 100 characters.")
        String entityName,

        @Schema(
                description = "Identifier of the entity being audited. Supports UUID, Long, or composite keys.",
                example = "42"
        )
        @NotBlank(message = "Entity ID is required.")
        @Size(max = 200, message = "Entity ID must not exceed 200 characters.")
        String entityId,

        @Schema(
                description = "Action performed on the entity.",
                example = "UPDATE"
        )
        @NotBlank(message = "Action is required.")
        @Size(max = 50, message = "Action must not exceed 50 characters.")
        String action,

        @Schema(
                description = "JSON snapshot of the entity BEFORE the action.",
                example = "{\"name\": \"Old Name\", \"email\": \"old@example.com\"}"
        )
        String beforeState,

        @Schema(
                description = "JSON snapshot of the entity AFTER the action.",
                example = "{\"name\": \"New Name\", \"email\": \"new@example.com\"}"
        )
        String afterState,

        @Schema(
                description = "ID of the user who performed the action.",
                example = "7"
        )
        @NotNull(message = "User ID is required.")
        Long userId
) {
}
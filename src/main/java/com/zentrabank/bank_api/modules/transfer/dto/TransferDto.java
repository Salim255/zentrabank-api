package com.zentrabank.bank_api.modules.transfer.dto;

import com.zentrabank.bank_api.modules.transfer.entity.TransferStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransferDto(

        @Schema(description = "Unique ID of the transfer")
        UUID id,

        @Schema(description = "Sender account number")
        String fromAccountNumber,

        @Schema(description = "Receiver internal account number")
        String toAccountNumber,

        @Schema(description = "External IBAN (null for internal transfers)")
        String externalIban,

        @Schema(description = "External BIC (null for internal transfers)")
        String externalBic,

        @Schema(description = "External recipient name (null for internal transfers)")
        String externalRecipientName,

        @Schema(description = "Amount transferred")
        BigDecimal amount,

        @Schema(description = "Currency of the transfer", example = "USD")
        String currency,

        @Schema(description = "Transfer status (PENDING, PROCESSING, COMPLETED, FAILED)")
        TransferStatus status,

        @Schema(description = "Unique reference for audit, idempotency, and external systems")
        String referenceId,

        @Schema(description = "Optional description provided by the user")
        String description,

        @Schema(description = "Timestamp when the transfer was created")
        Instant createdAt,

        @Schema(description = "Timestamp when the transfer was completed (null if not completed)")
        Instant completedAt
) {
}
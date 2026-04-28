package com.zentrabank.bank_api.modules.transfer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransferDto(
        // ---------------------------------------------------------
        // INTERNAL TRANSFER: sender account
        // ---------------------------------------------------------
        @NotNull(message = "Sender account number is required")
        @Schema(description = "Account number from which money will be sent")
        String fromAccountNumber,

        // ---------------------------------------------------------
        // EXTERNAL TRANSFER FIELDS
        // Only required when toAccountId is null
        // ---------------------------------------------------------
        @Schema(description = "External IBAN for external transfers")
        @Size(min = 15, max = 34, message = "IBAN must be between 15 and 34 characters")
        String externalIban,

        @Schema(description = "External BIC for external transfers")
        @Size(min = 8, max = 11, message = "BIC must be 8 or 11 characters")
        String externalBic,

        @Schema(description = "Recipient name for external transfers")
        @Size(max = 80, message = "Recipient name must not exceed 80 characters")
        String externalRecipientName,

        // ---------------------------------------------------------
        // AMOUNT
        // ---------------------------------------------------------
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        @Schema(description = "Amount to transfer", example = "250.00")
        BigDecimal amount,

        // ---------------------------------------------------------
        // CURRENCY
        // ---------------------------------------------------------
        @NotBlank(message = "Currency is required")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO code")
        @Schema(description = "Currency code (ISO 4217)", example = "USD")
        String currency,

        // ---------------------------------------------------------
        // OPTIONAL DESCRIPTION
        // ---------------------------------------------------------
        @Schema(description = "Optional transfer description", example = "Invoice payment")
        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description
) {
}
package com.zentrabank.bank_api.modules.transaction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "DTO representing a money transfer request.")
public record TransferDto(

        @Schema(
                description = "Sender's internal account number (the account to debit).",
                example = "FR7612345987650123456789012"
        )
        @NotBlank(message = "Sender account number is required.")
        @Size(max = 34, message = "Sender account number cannot exceed 34 characters.")
        @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Sender account number must contain only letters and digits.")
        String referenceAccountNumber,

        @Schema(
                description = "Recipient full name.",
                example = "John Doe"
        )
        @NotBlank(message = "Recipient name is required.")
        @Size(min = 2, max = 80, message = "Recipient name must be between 2 and 80 characters.")
        @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ' -]+$", message = "Recipient name contains invalid characters.")
        String recipientName,

        @Schema(
                description = "Recipient IBAN.",
                example = "DE89370400440532013000"
        )
        @NotBlank(message = "IBAN is required.")
        @Size(min = 15, max = 34, message = "IBAN length must be between 15 and 34 characters.")
        @Pattern(regexp = "^[A-Z]{2}[0-9A-Z]{13,30}$", message = "Invalid IBAN format.")
        String iban,

        @Schema(
                description = "Recipient BIC/SWIFT code.",
                example = "DEUTDEFF"
        )
        @NotBlank(message = "BIC is required.")
        @Pattern(
                regexp = "^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$",
                message = "Invalid BIC format."
        )
        String bic,

        @Schema(
                description = "Amount to transfer.",
                example = "250.00"
        )
        @NotNull(message = "Amount is required.")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero.")
        @DecimalMax(value = "1000000", message = "Amount exceeds the allowed transfer limit.")
        BigDecimal amount,

        @Schema(
                description = "Type of transfer (e.g., TRANSFER, SEPA_INSTANT).",
                example = "TRANSFER"
        )
        @NotBlank(message = "Transfer type is required.")
        String type,

        @Schema(
                description = "Optional transfer description.",
                example = "Invoice payment for March"
        )
        @Size(max = 140, message = "Description cannot exceed 140 characters.")
        String description
) {}

package com.zentrabank.bank_api.modules.profile.dto;

import com.zentrabank.bank_api.modules.profile.entity.EmploymentStatus;
import com.zentrabank.bank_api.modules.profile.entity.PersonTitle;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

public record ProfileDto(
        @Schema(description = "Unique profile identifier", example = "3f2c1b4e-8d9a-4c1e-9f2b-123456789abc")
        UUID id,

        @Schema(description = "Customer title", example = "MR")
        PersonTitle title,

        @Schema(description = "Customer first name", example = "John")
        String firstName,

        @Schema(description = "Customer last name", example = "Doe")
        String lastName,

        @Schema(description = "Date of birth in ISO-8601 format.", example = "1988-05-12")
        LocalDate dateOfBirth,

        @Schema(description = "Primary address line", example = "123 Main Street")
        String addressLine,

        @Schema(description = "City of residence", example = "New York")
        String city,

        @Schema(description = "Country of residence", example = "United States")
        String country,

        @Schema(description = "Postal or ZIP code", example = "10001")
        String zipCode,

        @Schema(description = "Customer phone number", example = "+1 555 123 4567")
        String phoneNumber,

        @Schema(description = "Type of phone number", example = "Mobile")
        String phoneType,

        @Schema(description = "Employment status", example = "EMPLOYED")
        EmploymentStatus employmentStatus
) {
}
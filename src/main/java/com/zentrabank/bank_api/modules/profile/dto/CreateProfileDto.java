package com.zentrabank.bank_api.modules.profile.dto;

import com.zentrabank.bank_api.modules.profile.entity.EmploymentStatus;
import com.zentrabank.bank_api.modules.profile.entity.PersonTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateProfileDto(
        @Schema(description = "Customer title", example = "MR")
        @NotNull(message = "Title is required")
        PersonTitle title,

        @Schema(description = "Customer first name", example = "John")
        @NotBlank(message = "First name is required")
        @Size(max = 100, message = "First name must be at most 100 characters")
        String firstName,

        @Schema(description = "Customer last name", example = "Doe")
        @NotBlank(message = "Last name is required")
        @Size(max = 100, message = "Last name must be at most 100 characters")
        String lastName,

        @Schema(
                description = "Customer date of birth",
                example = "1988-05-12",
                type = "string",
                format = "date"
        )
        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @Schema(description = "Primary address line", example = "123 Main Street")
        @NotBlank(message = "Address line is required")
        @Size(max = 150, message = "Address line must be at most 150 characters")
        String addressLine,

        @Schema(description = "City of residence", example = "New York")
        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must be at most 100 characters")
        String city,

        @Schema(description = "Country of residence", example = "United States")
        @NotBlank(message = "Country is required")
        @Size(max = 100, message = "Country must be at most 100 characters")
        String country,

        @Schema(description = "Postal or ZIP code", example = "10001")
        @NotBlank(message = "Zip code is required")
        @Size(max = 20, message = "Zip code must be at most 20 characters")
        String zipCode,

        @Schema(description = "Customer phone number", example = "+1 555 123 4567")
        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^[0-9+()\\-\\s]{6,20}$",
                message = "Invalid phone number format"
        )
        String phoneNumber,

        @Schema(description = "Type of phone number", example = "Mobile")
        @NotBlank(message = "Phone type is required")
        @Size(max = 20, message = "Phone type must be at most 20 characters")
        String phoneType,

        @Schema(description = "Employment status", example = "EMPLOYED")
        @NotNull(message = "Employment status is required")
        EmploymentStatus employmentStatus
) {}
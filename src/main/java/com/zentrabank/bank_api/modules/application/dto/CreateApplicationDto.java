package com.zentrabank.bank_api.modules.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateApplicationDto(
    @Schema(description = "Customer title (e.g. Mr, Ms, Dr).", example = "Mr")
    String title,

    @NotBlank
    @Size(min = 2, max = 50)
    @Schema(description = "Customer first name.", example = "John")
    String firstName,

    @NotBlank
    @Size(min = 2, max = 50)
    @Schema(description = "Customer last name.", example = "Doe")
    String lastName,

    @Schema(description = "Address line.", example = "123 Main Street")
    String addressLine,

    @NotBlank
    @Schema(description = "City of residence.", example = "Paris")
    String city,

    @NotBlank
    @Schema(description = "Postal code.", example = "75001")
    String zipCode,

    @NotBlank
    @Schema(description = "Phone type.", example = "MOBILE")
    String phoneType,

    @NotBlank
    @Schema(description = "Phone number in E.164 format.", example = "+33123456789")
    String phoneNumber,

    @Schema(description = "Date of birth in ISO-8601 format.", example = "1988-05-12")
    String dob,

    @Schema(description = "Employment status.", example = "EMPLOYED")
    String employment,

    @Schema(description = "Requested account type.", example = "CHECKING")
    String accountType,

    @NotEmpty
    @Schema(description = "Selected sources of wealth.", example = "[\"EMPLOYMENT_INCOME\",\"SAVINGS\"]")
    List<String> sourceOfWealth,

    @NotBlank
    @Schema(description = "Electronic signature (full legal name).", example = "John Doe")
    String signature

    // getters/setters
) {}
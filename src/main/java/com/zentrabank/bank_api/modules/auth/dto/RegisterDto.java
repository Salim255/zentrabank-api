package com.zentrabank.bank_api.modules.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO used when a new user registers an account.
 * Contains only the fields required for registration.
 */
public record RegisterDto(

        @Schema(example = "john.doe@example.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @Schema(example = "John")
        @NotBlank(message = "First name is required")
        String firstName,

        @Schema(example = "Doe")
        @NotBlank(message = "Last name is required")
        String lastName
) {}

package com.zentrabank.bank_api.modules.application.dto;

import com.zentrabank.bank_api.modules.account.dto.CreateAccountDto;
import com.zentrabank.bank_api.modules.account.entity.Account.AccountType;
import com.zentrabank.bank_api.modules.profile.dto.CreateProfileDto;
import com.zentrabank.bank_api.modules.profile.entity.EmploymentStatus;
import com.zentrabank.bank_api.modules.profile.entity.PersonTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CreateApplicationDto(
    @Schema(description = "Customer title (e.g. Mr, Ms, Dr).", example = "Mr")
    PersonTitle title,

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
    @Schema(description = "Country of residence.", example = "France")
    String country,

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
    EmploymentStatus employmentStatus,

    @Schema(description = "Requested account type.", example = "CHECKING")
    AccountType accountType,

    @NotEmpty
    @Schema(description = "Selected sources of wealth.", example = "[\"EMPLOYMENT_INCOME\",\"SAVINGS\"]")
    List<String> sourceOfWealth,

    @NotBlank
    @Schema(description = "Electronic signature (full legal name).", example = "John Doe")
    String signature

        // getters/setters
) {
    public CreateProfileDto toProfileDto() {
        return new CreateProfileDto(
                title,
                firstName,
                lastName,
                LocalDate.parse(dob),
                addressLine,
                city,
                country,
                zipCode,
                phoneNumber,
                phoneType,
                employmentStatus
        );
    }

    public CreateAccountDto toAccountDto(Long profileId) {
        return new CreateAccountDto(
                accountType
        );
    }
}
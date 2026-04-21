package com.zentrabank.bank_api.modules.application.validation;

import com.zentrabank.bank_api.exceptions.BadRequestException;
import com.zentrabank.bank_api.exceptions.InvalidFieldException;
import com.zentrabank.bank_api.modules.application.dto.CreateApplicationDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component // Marks this class as a Spring bean so it can be injected and managed by Spring
@RequiredArgsConstructor
public class ApplicationValidator {

    private final Logger logger = LoggerFactory.getLogger(ApplicationValidator.class);

    public void validate(CreateApplicationDto payload) {
        try {
            validateNames(payload);
            validateDob(payload);
            validatePhone(payload);
            validateAddress(payload);
            validateAccountType(payload);
            validateSourceOfWealth(payload);
            validateSignature(payload);

        } catch (RuntimeException ex) {
            logger.error("Error during application validation: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    // -----------------------------
    // FIELD VALIDATIONS
    // -----------------------------

    private void validateNames(CreateApplicationDto payload) {
        if (isBlank(payload.firstName()) || isBlank(payload.lastName())) {
            throw new BadRequestException("First name and last name are required");
        }

        if (payload.firstName().length() > 100 || payload.lastName().length() > 100) {
            throw new InvalidFieldException("Name fields exceed maximum length");
        }
    }

    private void validateDob(CreateApplicationDto payload) {
        if (isBlank(payload.dob())) {
            throw new BadRequestException("Date of birth is required");
        }

        try {
            LocalDate dob = LocalDate.parse(payload.dob());
            if (!dob.isBefore(LocalDate.now())) {
                throw new InvalidFieldException("Date of birth must be in the past");
            }
        } catch (Exception e) {
            throw new InvalidFieldException("Date of birth must follow ISO format YYYY-MM-DD");
        }
    }

    private void validatePhone(CreateApplicationDto payload) {
        if (isBlank(payload.phoneNumber()) || isBlank(payload.phoneType())) {
            throw new BadRequestException("Phone number and phone type are required");
        }

        if (!payload.phoneNumber().matches("^\\+?[0-9]{7,15}$")) {
            throw new InvalidFieldException("Invalid phone number format");
        }
    }

    private void validateAddress(CreateApplicationDto payload) {
        if (isBlank(payload.addressLine()) ||
                isBlank(payload.city()) ||
                isBlank(payload.zipCode())) {
            throw new BadRequestException("Address, city, and postal code are required");
        }
    }

    private void validateAccountType(CreateApplicationDto payload) {
        if (isBlank(String.valueOf(payload.accountType()))) {
            throw new BadRequestException("Account type is required");
        }
    }

    private void validateSourceOfWealth(CreateApplicationDto payload) {
        if (payload.sourceOfWealth() == null || payload.sourceOfWealth().isEmpty()) {
            throw new BadRequestException("At least one source of wealth must be selected");
        }
    }

    private void validateSignature(CreateApplicationDto payload) {
        if (isBlank(payload.signature())) {
            throw new BadRequestException("Signature is required");
        }

        String expected = payload.firstName() + " " + payload.lastName();
        if (!payload.signature().trim().equalsIgnoreCase(expected.trim())) {
            throw new InvalidFieldException("Signature must match your full legal name");
        }
    }

    // -----------------------------
    // UTIL
    // -----------------------------

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
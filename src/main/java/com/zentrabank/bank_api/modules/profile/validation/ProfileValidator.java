package com.zentrabank.bank_api.modules.profile.validation;

import com.zentrabank.bank_api.exceptions.BadRequestException;
import com.zentrabank.bank_api.exceptions.InvalidFieldException;
import com.zentrabank.bank_api.exceptions.NotFoundException;
import com.zentrabank.bank_api.modules.profile.dto.CreateProfileDto;
import com.zentrabank.bank_api.modules.user.entity.User;
import com.zentrabank.bank_api.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProfileValidator {

    private final Logger logger = LoggerFactory.getLogger(ProfileValidator.class);
    private final UserRepository userRepository;

    // ------------------------------------------------------------
    // VALIDATE PROFILE CREATION
    // ------------------------------------------------------------
    public void validateCreateProfile(CreateProfileDto payload, UUID userId) {
        try {
            if (payload == null) {
                throw new BadRequestException("Profile payload cannot be null");
            }

            if (userId == null) {
                throw new BadRequestException("UserId cannot be null");
            }

            // Validate user exists
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            // Validate required fields
            validateString(payload.firstName(), "First name");
            validateString(payload.lastName(), "Last name");
            validateString(payload.addressLine(), "Address line");
            validateString(payload.city(), "City");
            validateString(payload.country(), "Country");
            validateString(payload.zipCode(), "Zip code");
            validateString(payload.phoneNumber(), "Phone number");
            validateString(payload.phoneType(), "Phone type");

            // Validate enums
            if (payload.title() == null) {
                throw new InvalidFieldException("Title is required");
            }

            if (payload.employmentStatus() == null) {
                throw new InvalidFieldException("Employment status is required");
            }

            // Validate phone format
            if (!payload.phoneNumber().matches("^[0-9+()\\-\\s]{6,20}$")) {
                throw new InvalidFieldException("Invalid phone number format");
            }

        } catch (RuntimeException ex) {
            logger.error("Error during profile validation: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    // ------------------------------------------------------------
    // VALIDATE PROFILE UPDATE
    // ------------------------------------------------------------
    public void validateUpdateProfile(CreateProfileDto payload) {
        try {
            if (payload == null) {
                throw new BadRequestException("Profile payload cannot be null");
            }

            validateString(payload.firstName(), "First name");
            validateString(payload.lastName(), "Last name");
            validateString(payload.addressLine(), "Address line");
            validateString(payload.city(), "City");
            validateString(payload.country(), "Country");
            validateString(payload.zipCode(), "Zip code");
            validateString(payload.phoneNumber(), "Phone number");
            validateString(payload.phoneType(), "Phone type");

            if (payload.title() == null) {
                throw new InvalidFieldException("Title is required");
            }

            if (payload.employmentStatus() == null) {
                throw new InvalidFieldException("Employment status is required");
            }

            if (!payload.phoneNumber().matches("^[0-9+()\\-\\s]{6,20}$")) {
                throw new InvalidFieldException("Invalid phone number format");
            }

        } catch (RuntimeException ex) {
            logger.error("Error during profile update validation: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    // ------------------------------------------------------------
    // HELPER: VALIDATE STRING FIELD
    // ------------------------------------------------------------
    private void validateString(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidFieldException(fieldName + " is required");
        }
    }
}

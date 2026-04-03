package com.zentrabank.bank_api.modules.auth.validation;

import com.zentrabank.bank_api.exceptions.EmailAlreadyUsedException;
import com.zentrabank.bank_api.exceptions.WeakPasswordException;
import com.zentrabank.bank_api.modules.auth.dto.RegisterDto;
import com.zentrabank.bank_api.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component // Marks this class as a Spring bean so it can be injected and managed by Spring
@RequiredArgsConstructor // This is a Lombok annotation that automatically generates a constructor with all final fields.
public class RegisterValidator {
    private final UserRepository userRepository;

    public void validate(RegisterDto payload) {

        if (userRepository.existsByEmail(payload.email())) {
            throw new EmailAlreadyUsedException("Email already exists");
        }

        if (payload.password().length() < 8) {
            throw new WeakPasswordException("Password must be at least 8 characters");
        }

        // more rules...
    }
}
package com.zentrabank.bank_api.modules.auth.validation;

import com.zentrabank.bank_api.exceptions.EmailAlreadyUsedException;
import com.zentrabank.bank_api.modules.auth.dto.RegisterDto;
import com.zentrabank.bank_api.modules.auth.dto.ResetPasswordDto;
import com.zentrabank.bank_api.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component // Marks this class as a Spring bean so it can be injected and managed by Spring
@RequiredArgsConstructor // This is a Lombok annotation that automatically generates a constructor with all final fields.
public class AuthValidator {
    private final Logger logger = LoggerFactory.getLogger(AuthValidator.class);
    private final UserRepository userRepository;

    public void comparePassword(ResetPasswordDto payload){

    }

    public void registerValidate(RegisterDto payload) {

        if (this.userRepository.existsByEmail(payload.email())) {
            this.logger.error("Hello from in validator 👹👹👹👹👹");
            throw new EmailAlreadyUsedException("Email already exists");
        }

        // more rules...
    }
}
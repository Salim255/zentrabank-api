package com.zentrabank.bank_api.modules.auth.validation;

import com.zentrabank.bank_api.exceptions.EmailAlreadyUsedException;
import com.zentrabank.bank_api.exceptions.InvalidEmailException;
import com.zentrabank.bank_api.modules.auth.dto.LoginDto;
import com.zentrabank.bank_api.modules.auth.dto.RegisterDto;
import com.zentrabank.bank_api.modules.auth.dto.ResetPasswordDto;
import com.zentrabank.bank_api.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component // Marks this class as a Spring bean so it can be injected and managed by Spring
@RequiredArgsConstructor // This is a Lombok annotation that automatically generates a constructor with all final fields.
public class AuthValidator {
    private final Logger logger = LoggerFactory.getLogger(AuthValidator.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void comparePassword(String textPassword, String hashedPassword){
        boolean isMatch = passwordEncoder.matches(textPassword, hashedPassword);

        if (!isMatch){

        }
    }

    public void registerValidate(RegisterDto payload) {
      try {
          //  Validate email format
          String email = payload.email();
          if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
              throw new InvalidEmailException("Email format is invalid");
          }

          // Check email uniqueness
          if (this.userRepository.existsByEmail(payload.email())) {
              throw new EmailAlreadyUsedException("Email already exists");
          }
      } catch (RuntimeException ex){
          // Log error here for debugging / monitoring
          this.logger.error("Error during register validation: {}", ex.getMessage(), ex);
          throw ex;
      }
    }

    public void loginValidate(LoginDto payload){

    }
}
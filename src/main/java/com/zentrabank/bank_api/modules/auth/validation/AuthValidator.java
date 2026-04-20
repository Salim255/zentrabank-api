package com.zentrabank.bank_api.modules.auth.validation;

import com.zentrabank.bank_api.exceptions.*;
import com.zentrabank.bank_api.modules.auth.dto.LoginDto;
import com.zentrabank.bank_api.modules.auth.dto.RegisterDto;
import com.zentrabank.bank_api.modules.user.entity.User;
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
            throw  new InvalidPasswordException("Invalid login credentials");
        }
    }

    public void registerValidate(RegisterDto payload) {
      try {

          String email = payload.email();

          boolean isInValidEmail = email == null || email.isBlank();


          if (isInValidEmail) {
              throw new BadRequestException("Invalid field in register user");
          }

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

    public User loginValidate(LoginDto payload){
        try{

            String userName = payload.userName();
            String password = payload.password();
            boolean isUserNameInvalid = userName == null || userName.isBlank();
            boolean isPasswordInvalid = password == null || password.isBlank();

            if (isUserNameInvalid || isPasswordInvalid){
                throw new UnauthorizedException("Invalid login credentials");
            }

            User user = this.userRepository.findByUserName(userName)
                    .orElseThrow(() -> new NotFoundException("User with this userName does not exist"));

            // Validate password
            this.comparePassword(payload.password(), user.getPasswordHash());

            // Return user
            return  user;
        } catch (RuntimeException ex){
            this.logger.error("Error during login validation: {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}
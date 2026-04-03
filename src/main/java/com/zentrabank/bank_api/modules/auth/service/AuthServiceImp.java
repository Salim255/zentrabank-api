package com.zentrabank.bank_api.modules.auth.service;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.exceptions.EmailAlreadyUsedException;
import com.zentrabank.bank_api.exceptions.WeakPasswordException;
import com.zentrabank.bank_api.modules.auth.dto.LoginDto;
import com.zentrabank.bank_api.modules.auth.dto.LoginResponseDto;
import com.zentrabank.bank_api.modules.auth.dto.RegisterDto;
import com.zentrabank.bank_api.modules.auth.dto.RegisterResponseDto;
import com.zentrabank.bank_api.modules.user.entity.User;
import com.zentrabank.bank_api.modules.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthServiceImp implements AuthService {
    private final Logger logger = LoggerFactory.getLogger(AuthServiceImp.class);

    private final UserRepository userRepository;

    public AuthServiceImp(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public ApiResponseDto<RegisterResponseDto> register(RegisterDto payload){
        try{
            RegisterResponseDto response = new RegisterResponseDto("hello");
            // 1 Validate user input;

            // 2 Create User class
            User createdUser = new User(payload);
            return  ApiResponseDto.success(response);
        } catch (DataIntegrityViolationException ex){
            logger.error("Database constraint violation during user registration", ex);
            // Unknown constraint → let GlobalExceptionHandler handle it
            throw ex;
        }
    }

    public  ApiResponseDto<LoginResponseDto> login(LoginDto payload)
    {
        try {
            LoginResponseDto response = new LoginResponseDto("Hello from login");
            return  ApiResponseDto.success(response);
        } catch (DataIntegrityViolationException ex){
            logger.error("Database constraint violation during user login", ex);
            // Unknown constraint → let GlobalExceptionHandler handle it
            throw ex;
        }
    }

    private String generateUniqueLoginId() {
        String id;
        do {
            id = generate9DigitNumber();
        } while (userRepository.existsByLoginId(id));
        return id;
    }

    private String generate9DigitNumber() {
        int min = 100_000_000;
        int max = 999_999_999;
        int number = min + new Random().nextInt(max - min);
        return String.valueOf(number);
    }

    private String generateTempPassword() {
        int min = 100_000; // smallest 6-digit number
        int max = 999_999; // largest 6-digit number
        int number = min + new Random().nextInt(max - min);
        return String.valueOf(number);
    }

    private void validateRegisterPayload(RegisterDto payload) {

        if (userRepository.existsByEmail(payload.email())) {
            throw new EmailAlreadyUsedException("Email already exists");
        }

        if (payload.password().length() < 8) {
            throw new WeakPasswordException("Password must be at least 8 characters");
        }

        // Add more business rules here
    }

}
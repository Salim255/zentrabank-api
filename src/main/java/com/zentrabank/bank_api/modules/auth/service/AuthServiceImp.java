package com.zentrabank.bank_api.modules.auth.service;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.exceptions.EmailAlreadyUsedException;
import com.zentrabank.bank_api.exceptions.WeakPasswordException;
import com.zentrabank.bank_api.modules.auth.dto.LoginDto;
import com.zentrabank.bank_api.modules.auth.dto.LoginResponseDto;
import com.zentrabank.bank_api.modules.auth.dto.RegisterDto;
import com.zentrabank.bank_api.modules.auth.dto.RegisterResponseDto;
import com.zentrabank.bank_api.modules.auth.validation.RegisterValidator;
import com.zentrabank.bank_api.modules.user.entity.User;
import com.zentrabank.bank_api.modules.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthServiceImp implements AuthService {
    private final Logger logger = LoggerFactory.getLogger(AuthServiceImp.class);
    private final RegisterValidator registerValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImp(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            RegisterValidator registerValidator
    ){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.registerValidator = registerValidator;
    }

    @Override
    public ApiResponseDto<RegisterResponseDto> register(RegisterDto payload){
        try{
            // 1 Validate user input;
            this.registerValidator.validate(payload);

            // 2 Generated temp password
            String tmpPassword = this.generateTempPassword();

            // 3 Hash password
            String hashed = this.passwordEncoder.encode(tmpPassword);

            // 4 Generate user loginId
            String userLoginId = this.generateUniqueLoginId();

            // 5 Create User class
            User createdUser = new User();
            createdUser.setEmail(payload.email());
            createdUser.setFirstName(payload.firstName());
            createdUser.setLastName(payload.lastName());
            createdUser.setPasswordHash(hashed);
            createdUser.setLoginId(userLoginId);


            // 5 Save the
            this.userRepository.save(createdUser);

            //
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
}
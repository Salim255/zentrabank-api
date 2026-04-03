package com.zentrabank.bank_api.modules.auth.service;

import com.zentrabank.bank_api.common.dto.ApiResponseDto;
import com.zentrabank.bank_api.modules.auth.dto.RegisterDto;
import com.zentrabank.bank_api.modules.auth.dto.RegisterResponseDto;
import com.zentrabank.bank_api.modules.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            return  ApiResponseDto.success(response);
        } catch (Exception error){
            logger.error("Error in create user", error);
            throw error;
        }
    }

    public  String login(){
        return  "Hello from login";
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
package com.zentrabank.bank_api.modules.auth.service;

import com.zentrabank.bank_api.modules.user.repository.UserRepository;

import java.util.Random;

public class AuthServiceImp implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImp(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public String register(){
        return "Hello Register";
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
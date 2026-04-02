package com.zentrabank.bank_api.modules.auth.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/register")
    public String register(){
        return  "Hello register";
    }

    @PostMapping("/login")
    public String Login(){
        return  "Hello String";
    }
}
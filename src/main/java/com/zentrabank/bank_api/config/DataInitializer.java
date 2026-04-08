package com.zentrabank.bank_api.config;

import com.zentrabank.bank_api.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private  final UserService userService;
}
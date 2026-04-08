package com.zentrabank.bank_api.config;

import com.zentrabank.bank_api.modules.auth.service.AuthService;
import com.zentrabank.bank_api.modules.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private  final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private  final AuthService authService;

    @PostConstruct
    public void  init(){
        try {
            this.authService.createSuperAdmin();
        } catch (Exception e) {
            this.logger.error("Error form create super admin { }", e);
            throw new RuntimeException(e);
        }
    }
}
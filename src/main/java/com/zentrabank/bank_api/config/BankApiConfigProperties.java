package com.zentrabank.bank_api.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@ConfigurationProperties(prefix = "bank-api-secrets")
public record BankApiConfigProperties(
        String profileActive,
        String jwtSecret,
        Duration jwtExpiration,
        Duration refreshTokenExpiration,
        Duration jwtCookieExpireIn,
        String apiPrefix
){

}
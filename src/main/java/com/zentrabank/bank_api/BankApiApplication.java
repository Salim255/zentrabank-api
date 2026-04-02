package com.zentrabank.bank_api;

import com.zentrabank.bank_api.config.BankApiConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(BankApiConfigProperties.class)
@SpringBootApplication
public class BankApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(BankApiApplication.class, args);
	}
}

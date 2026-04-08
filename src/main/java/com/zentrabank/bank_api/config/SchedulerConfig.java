package com.zentrabank.bank_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling // Enables Spring's scheduling system (without this, @Scheduled does nothing)
public class SchedulerConfig {
    // No logic here
    // This class only tells Spring:
    // "Hey, I want to run background jobs using @Scheduled"
}
package com.zentrabank.bank_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Marks this class as a Spring configuration class.
// Spring will automatically detect it and load the beans inside.
@Configuration
public class CorsConfig {

    // This bean defines the CORS configuration for the entire application.
    // Spring Security will automatically use it when we call http.cors().
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        // This object holds all CORS rules (who can call your API, how, etc.)
        CorsConfiguration config = new CorsConfiguration();

        // Allowed origin (IMPORTANT)
        // This defines which frontend is allowed to communicate with your backend.
        // Here we allow Angular running on localhost:4200.
        // In production, replace this with your real domain.
        config.addAllowedOrigin("http://localhost:4200");

        // Allow credentials (VERY IMPORTANT for JWT in cookies)
        // This allows the browser to send cookies (like JWT tokens) with requests.
        // Without this, cookies will NOT be sent.
        config.setAllowCredentials(true);

        // Allowed headers
        // This allows all headers (Authorization, Content-Type, etc.)
        // You can restrict this later for more security if needed.
        config.addAllowedHeader("*");

        // Allowed HTTP methods
        // This allows all request types: GET, POST, PUT, DELETE, PATCH...
        // You can restrict this if your API needs tighter control.
        config.addAllowedMethod("*");

        // This object maps the CORS configuration to specific URL patterns.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Apply this CORS configuration to ALL endpoints (/**)
        // Meaning: every request in your API will follow these rules.
        source.registerCorsConfiguration("/**", config);

        // Return the configuration so Spring Security can use it.
        return source;
    }
}
package com.zentrabank.bank_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

// Marks this class as a configuration class.
// Spring will scan it and load the beans defined inside.
@Configuration

// Enables Spring Security for the application.
// Without this, the security filter chain would not be activated.
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // Allow frontend origin (Angular)
        config.addAllowedOrigin("http://localhost:4200");

        // Allow sending cookies (VERY IMPORTANT for JWT in cookies)
        config.setAllowCredentials(true);

        // Allow all headers (Authorization, Content-Type, etc.)
        config.addAllowedHeader("*");

        // Allow all HTTP methods (GET, POST, PUT, DELETE...)
        config.addAllowedMethod("*");

        // Apply this config to all endpoints
        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // creates a new instance of the BCryptPasswordEncoder
        // class from Spring Security.
        // What BCryptPasswordEncoder is
        // It’s a password hashing utility provided by Spring Security.
        // Uses the BCrypt algorithm, which is:
        // One-way: You can hash a password but cannot retrieve the original string from the hash.
        // Salted: Automatically adds a random salt to make the same
        // password produce different hashes each time.
        // Secure: Resistant to brute-force attacks due to configurable strength.
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtAuthFilter jwtAuthFilter,
            JwtAuthEntryPoint jwtAuthEntryPoint
            ) throws Exception {
        // "http" is the main security builder.
        // You configure all security rules on it.
        http
                // Enable CORS with our configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Ensures your JwtAuthFilter runs before Spring Security’s default authentication filter.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // All authentication exceptions (invalid/missing token) are handled by your
                // custom entry point, returning JSON responses.
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthEntryPoint)
                )
                // -------------------------------
                // 1. Disable CSRF
                // -------------------------------
                // CSRF protection is useful for browser-based sessions.
                // But for REST APIs using JWT, it must be disabled.
                .csrf(AbstractHttpConfigurer::disable)
                // -------------------------------
                // 2. Disable session creation
                // -------------------------------
                // We want a stateless API (like NestJS with JWT).
                // This tells Spring Security:
                // "Do NOT create or use HTTP sessions."
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // -------------------------------
                // 3. Authorization rules
                // -------------------------------
                // Here we define which endpoints are public
                // and which ones require authentication.
                .authorizeHttpRequests(auth -> auth
                        // Allow ALL requests under /auth/**
                        // (login, register, refresh, logout)
                        .requestMatchers("/auth/**").permitAll()

                        // Any other endpoint must be authenticated.
                        // Example: /users, /tasks, /admin, etc.
                        .anyRequest().authenticated()
                );
        // Build and return the configured security chain.
        // This activates all the rules above.
        return http.build();
    }
}
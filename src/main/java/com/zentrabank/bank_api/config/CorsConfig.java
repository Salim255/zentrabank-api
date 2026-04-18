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

        // Allowed origins
        // IMPORTANT:
        // We use addAllowedOriginPattern() instead of addAllowedOrigin() because
        // Spring Security blocks addAllowedOrigin() when credentials are enabled.
        // With credentials = true (required for cookies / JWT in cookies),
        // the browser requires an exact, explicit origin match and Spring requires
        // origin *patterns* instead of fixed origins.
        // This ensures:
        //   - Cookies (HttpOnly, Secure, SameSite) are sent correctly
        //   - Angular's `withCredentials: true` works
        //   - CORS preflight responses include the correct Access-Control-Allow-Origin
        //   - No wildcard origins are allowed (more secure)
        // In short: addAllowedOriginPattern() is the only correct method when
        // using cookies or authenticated requests across domains.
        config.addAllowedOriginPattern("http://localhost:4200");
        config.addAllowedOriginPattern("https://zentrabank.salimcode.site");

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
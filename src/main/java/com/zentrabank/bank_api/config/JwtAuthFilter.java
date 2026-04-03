package com.zentrabank.bank_api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Marks this class as a Spring-managed bean so it can be injected into SecurityConfig
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // This method runs ON EVERY REQUEST before it reaches your controller.
    // It is the Spring Boot equivalent of a NestJS Guard.
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Extract all cookies from the incoming HTTP request.
        // If the user is logged in, one of these cookies should contain the JWT.
        Cookie[] cookies = request.getCookies();

        // Only proceed if cookies exist (avoid NullPointerException)
        if (cookies != null) {

            // Loop through all cookies to find the one containing the JWT
            for (Cookie cookie : cookies) {

                // We check for the cookie name you defined for authentication
                if ("zentra_access_jwt".equals(cookie.getName())) {

                    // Extract the raw JWT token from the cookie
                    String token = cookie.getValue();

                    // Validate and decode the token using your JwtService
                    // This should return a UserDetails object
                    var userDetails = jwtService.parseToken(token);

                    // Create an Authentication object recognized by Spring Security
                    // This is equivalent to: req.user = user in NestJS
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,               // principal (the user)
                                    null,                      // no credentials needed
                                    userDetails.getAuthorities() // roles/permissions
                            );

                    // Attach the authenticated user to the Spring Security context
                    // This makes the user available in controllers via Authentication auth
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        // Continue the filter chain so the request reaches the controller
        filterChain.doFilter(request, response);
    }
}

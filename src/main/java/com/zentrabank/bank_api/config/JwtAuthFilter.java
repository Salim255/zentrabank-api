package com.zentrabank.bank_api.config;

// Import the JWT service responsible for decoding and validating tokens
import com.zentrabank.bank_api.config.JwtService;

// Custom exception thrown when authentication fails
import com.zentrabank.bank_api.exceptions.ForbiddenException;

// DTO returned by JwtService after decoding the token
import com.zentrabank.bank_api.modules.auth.dto.UserTokenDetailsDto;

// Spring + Servlet imports
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Spring Security imports for attaching authentication to the context
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

// Logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Marks this filter as a Spring-managed bean
import org.springframework.stereotype.Component;

// Ensures the filter runs once per request
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthFilter
 *
 * This filter executes BEFORE every controller request.
 * It is the Spring Boot equivalent of a NestJS Guard.
 *
 * Responsibilities:
 *  - Extract JWT from cookies
 *  - Validate + decode token
 *  - Extract userId
 *  - Attach ONLY userId to the SecurityContext
 *  - Reject request with 403 if token is missing or invalid
 *
 * This filter is PURE:
 *  - No DB calls
 *  - No roles
 *  - No UserDetailsService
 *  - Stateless
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    // Service responsible for decoding + validating JWTs
    private final JwtService jwtService;

    // Logger for debugging and error tracking
    private final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    // Constructor injection (cleaner, testable, recommended)
    public JwtAuthFilter(JwtService jwtService,  JwtAuthEntryPoint jwtAuthEntryPoint ){
        this.jwtService = jwtService;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    /**
     * doFilterInternal()
     *
     * This method runs for EVERY incoming HTTP request.
     * It decides whether the request is authenticated or not.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    )  throws ServletException, IOException {

        try {
            // Extract cookies from the incoming request.
            // If the user is authenticated, one cookie should contain the JWT.
            Cookie[] cookies = request.getCookies();

            // If there are no cookies at all → user is not authenticated → 403 Forbidden
            if (cookies == null) {
                throw new ForbiddenException("Missing authentication token");
            }

            String token = null;

            // Search for the JWT cookie by name
            for (Cookie cookie : cookies) {
                // "zentra_access_jwt" is the cookie name where the JWT is stored
                if ("zentra_access_jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }

            // Debug log (you can remove this)
            this.logger.error("Hello error from guard 👹👹👹👹👹👹");

            // If the JWT cookie is missing → 403 Forbidden
            if (token == null) {
                throw new ForbiddenException("Missing authentication token");
            }

            // Decode + validate the JWT using JwtService
            UserTokenDetailsDto tokenData = jwtService.parseToken(token);

            // If token does not contain a userId → invalid token → 403 Forbidden
            if (tokenData.userId() == null) {
                throw new ForbiddenException("Invalid authentication token");
            }

            /**
             * Attach ONLY the userId to the Spring Security context.
             *
             * principal = userId
             * credentials = null
             * authorities = null (no roles)
             *
             * This is intentionally minimal and stateless.
             */
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            tokenData.userId(), // principal
                            null,               // no password
                            null                // no roles
                    );

            // Store authentication in the SecurityContext so controllers can access it
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Continue the filter chain so the request reaches the controller
            filterChain.doFilter(request, response);

        } catch (ForbiddenException ex) {

            /**
             * IMPORTANT:
             * We catch ForbiddenException INSIDE the filter.
             *
             * Why?
             *  - If we let it bubble up, Tomcat logs a huge stack trace.
             *  - Spring Security logs it too.
             *  - Your terminal becomes noisy.
             *
             * By catching it here and manually calling the entry point,
             * we prevent ANY terminal logs.
             */
            jwtAuthEntryPoint.commence(
                    request,
                    response,
                    new org.springframework.security.core.AuthenticationException(ex.getMessage()) {}
            );
        }
    }
}

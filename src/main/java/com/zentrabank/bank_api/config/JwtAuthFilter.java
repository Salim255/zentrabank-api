package com.zentrabank.bank_api.config;

import com.zentrabank.bank_api.exceptions.ForbiddenException;
import com.zentrabank.bank_api.modules.auth.dto.UserTokenDetailsDto;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthFilter
 *
 * This filter runs BEFORE every request reaches your controllers.
 *
 * Its role is simple:
 *  - Check if the request has a valid JWT
 *  - If valid → attach userId to SecurityContext
 *  - If invalid → stop request and return 403
 *
 * Important:
 *  - This filter is STATELESS (no DB calls)
 *  - It only trusts the JWT
 *  - It does NOT manage roles or permissions
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    private final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    public JwtAuthFilter(JwtService jwtService, JwtAuthEntryPoint jwtAuthEntryPoint) {
        this.jwtService = jwtService;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    /**
     * shouldNotFilter()
     *
     * This method tells Spring:
     * 👉 "Do NOT run this filter for these routes"
     *
     * We skip authentication for public endpoints (login, signup, etc.)
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // Skip ALL authentication routes
        return path.startsWith("/auth/");
    }

    /**
     * doFilterInternal()
     *
     * This method is executed for every protected request.
     *
     * Flow:
     *  1. Clear any previous authentication (safety)
     *  2. Extract JWT from cookies
     *  3. Validate token
     *  4. Attach userId to SecurityContext
     *  5. Continue request
     *
     * If ANY step fails → stop request and return 403
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Always start clean (avoid leaking authentication between requests)
        SecurityContextHolder.clearContext();

        try {
            Cookie[] cookies = request.getCookies();

            String token = null;

            /**
             * Extract JWT from cookies
             *
             * We look for a cookie named "zentra_access_jwt"
             */
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("zentra_access_jwt".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }

            // If no token found → reject request
            if (token == null) {
                throw new ForbiddenException("Missing authentication token");
            }

            // Debug (use debug level, not error)
            logger.debug("JWT extracted");

            /**
             * Validate and decode the token
             *
             * JwtService is responsible for:
             *  - checking signature
             *  - checking expiration
             *  - extracting payload
             */
            UserTokenDetailsDto tokenData = jwtService.parseToken(token);

            // If token does not contain userId → invalid
            if (tokenData.userId() == null) {
                throw new ForbiddenException("Invalid authentication token");
            }

            /**
             * Create Authentication object
             *
             * principal = userId
             * credentials = null (we don't use password here)
             * authorities = null (no roles yet)
             */
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            tokenData.userId(),
                            null,
                            null
                    );

            // Store authentication so controllers can access it
            SecurityContextHolder.getContext().setAuthentication(auth);

            /**
             * Continue the request
             *
             * VERY IMPORTANT:
             * If we don't call this → request stops here
             */
            filterChain.doFilter(request, response);

        } catch (ForbiddenException ex) {

            /**
             * Authentication failed
             *
             * Clean context to ensure no invalid data remains
             */
            SecurityContextHolder.clearContext();

            /**
             * Delegate error handling to JwtAuthEntryPoint
             *
             * Why:
             *  - Ensures consistent JSON response
             *  - Avoids default HTML error page
             */
            jwtAuthEntryPoint.commence(
                    request,
                    response,
                    new org.springframework.security.core.AuthenticationException(ex.getMessage()) {}
            );
        }
    }
}
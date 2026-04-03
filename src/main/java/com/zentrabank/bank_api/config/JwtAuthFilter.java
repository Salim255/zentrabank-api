package com.zentrabank.bank_api.config;

import com.zentrabank.bank_api.config.JwtService;
import com.zentrabank.bank_api.exceptions.ForbiddenException;
import com.zentrabank.bank_api.modules.auth.dto.UserTokenDetailsDto;
import com.zentrabank.bank_api.modules.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthFilter
 *
 * This filter runs BEFORE every controller request.
 * It is the Spring Boot equivalent of a NestJS Guard.
 *
 * Responsibilities:
 *  - Read JWT from cookies
 *  - Validate + decode token
 *  - Extract userId (and expireIn)
 *  - Attach ONLY userId to the SecurityContext
 *  - Reject request with 403 if token is missing or invalid
 *
 * This filter is PURE: no DB calls, no roles, no UserDetails.
 */

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    public JwtAuthFilter(JwtService jwtService ){
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    )  throws ServletException, IOException {

        // Extract cookies from the incoming request.
        // If the user is authenticated, one cookie should contain the JWT.
        Cookie[] cookies = request.getCookies();

        // If there are no cookies at all → user is not authenticated → 403 Forbidden
        if (cookies == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String token = null;

        // Search for the JWT cookie by name
        for (Cookie cookie : cookies) {
            if ("zentra_access_jwt".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }

        this.logger.error("Hello error fom guard 👹👹👹👹👹👹");
        // If the JWT cookie is missing → 403 Forbidden
        if (token == null) {
            throw  new ForbiddenException("Missing authentication token");
            //return;
        }

        try {
            // Decode + validate the JWT using JwtService
            UserTokenDetailsDto tokenData = jwtService.parseToken(token);

            // If token does not contain a userId → invalid token → 403 Forbidden
            if (tokenData.userId() == null) {
                throw  new ForbiddenException("Invalid authentication token");
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
                            tokenData.userId(),
                            null,
                            null
                    );

            // Store authentication in the SecurityContext so controllers can access it
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            // Any exception during parsing means invalid token → 403 Forbidden
            throw new ForbiddenException("Invalid or expired authentication token");
        }

        // Continue the filter chain so the request reaches the controller
        filterChain.doFilter(request, response);
    }
}
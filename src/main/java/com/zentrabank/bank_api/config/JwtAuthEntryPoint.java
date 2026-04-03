package com.zentrabank.bank_api.config;

// Import the HTTP request/response objects used by Spring Security filters
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Import the exception thrown when authentication fails inside Spring Security
import org.springframework.security.core.AuthenticationException;

// Import the interface we must implement to customize authentication failure handling
import org.springframework.security.web.AuthenticationEntryPoint;

// Marks this class as a Spring-managed bean so it can be injected automatically
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

/**
 * JwtAuthEntryPoint
 *
 * This class is responsible for handling authentication failures that occur
 * inside the Spring Security filter chain.
 *
 * Why we need it:
 *  - When JwtAuthFilter throws ForbiddenException or AuthenticationException,
 *    Spring Security intercepts it BEFORE it reaches your GlobalExceptionHandler.
 *
 *  - Without this class, Spring Security returns a default HTML error page
 *    or a raw stack trace — not your clean JSON API format.
 *
 *  - This entry point ensures ALL authentication errors return a consistent,
 *    JSON-formatted response that matches the rest of your API.
 *
 * What breaks if removed:
 *  - Your API will return ugly HTML error pages for 401/403 errors.
 *  - Your frontend will not receive JSON.
 *  - Your GlobalExceptionHandler will NOT catch authentication errors.
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    private final BankApiConfigProperties config;

    public JwtAuthEntryPoint(BankApiConfigProperties config){
        this.config = config;
    }

    private boolean isDev(){
        return this.config.profileActive().equals("dev");
    }
    /**
     * commence()
     *
     * This method is automatically called by Spring Security whenever:
     *  - authentication fails
     *  - a protected endpoint is accessed without a valid token
     *  - JwtAuthFilter throws an exception
     *
     * Parameters:
     *  - request: the incoming HTTP request
     *  - response: the outgoing HTTP response we will customize
     *  - authException: the exception thrown by Spring Security
     *
     * Why this method must exist:
     *  - AuthenticationEntryPoint is an interface with ONE abstract method.
     *  - If we don't implement it, the class must be abstract → compilation error.
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        // Set HTTP status code to 403 Forbidden
        // Why: user is authenticated incorrectly or missing token
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Tell the client that the response body is JSON
        response.setContentType("application/json");

        // Build a JSON error response manually
        // Why manually? Because we are inside the filter chain, not a controller.
        String json = """
        {
            "status": "error",
            "message": "Missing or invalid authentication token",
            "data": null
        }
        """;

        // If you want stack trace in DEV mode only
        if (isDev()) {
            json = """
            {
                "status": "error",
                "message": "Missing or invalid authentication token",
                "data": null,
                "stack": "%s"
            }
            """.formatted(Arrays.toString(authException.getStackTrace()));
        }

        // Write the JSON into the HTTP response body
        // If we don't do this, the client receives an empty response
        response.getWriter().write(json);
    }
}

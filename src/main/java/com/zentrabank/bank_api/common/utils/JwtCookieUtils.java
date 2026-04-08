package com.zentrabank.bank_api.common.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtCookieUtils {
    public static void clearJwtCookie(
            HttpServletResponse response,
            String name
    ){
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * Create a JWT cookie with secure defaults
     *
     * @param token JWT string
     * @param secure Whether to use HTTPS only (true in production)
     * @param maxAgeInSeconds Expiration time in seconds
     * @return Configured Cookie object
     */
    public static Cookie createJwtCookie(
            String token,
            boolean secure,
            int maxAgeInSeconds,
            String name
    ) {
        Cookie cookie = new Cookie(name, token); // cookie name "jwt"

        cookie.setHttpOnly(true);                  // protect against XSS
        cookie.setSecure(secure);                  // only over HTTPS in prod
        cookie.setPath("/");                        // available to all endpoints
        cookie.setMaxAge(maxAgeInSeconds);         // e.g., 1 day = 24*60*60 seconds

        return cookie;
    }

    /**
     * Extract JWT token from cookies
     */
    public static String extractJwt(HttpServletRequest request, String name) {

        if (request.getCookies() == null) {
            return null;
        }

        /**
         * Extract JWT from cookies
         *
         * We look for a cookie named "zentra_access_jwt"
         */
        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
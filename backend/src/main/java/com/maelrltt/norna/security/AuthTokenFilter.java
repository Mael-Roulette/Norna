package com.maelrltt.norna.security;

import com.maelrltt.norna.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Authentication filter responsible for processing JWT tokens on incoming HTTP requests
 * This filter is executed once per request and checks whether the request contains a valid JWT token in the Authorization header
 * If the token is valid, the corresponding user's information is loaded
 * and an authenticated SecurityContext is created
 */
@Component
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    // Prefix used in the Authorization HTTP header for Bearer tokens
    public static final String BEARER_ =  "Bearer ";

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtUtility  jwtUtility;

    /**
     * The method extracts the JWT from the Authorization header .
     * Checks whether the token exists and is valid.
     * The method extracts the username from the JWT and loads the corresponding user.
     * She creates an authenticated SecurityContext.
     * The continues the filter chain.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);

            if ( jwt != null && jwtUtility.validateToken(jwt) ) {
                // Extract the username
                final String username = jwtUtility.getUsernameFromToken(jwt);

                // Load user's information
                final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Create an Authentication object
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Attach additional information about the HTTP request
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Store the authenticated user in Spring Security's context.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            log.error("Cannot authenticate request", e);
        }

        // Continue processing the request through the remaining filters
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization HTTP header
     * @param request - The incoming HTTP request
     * @return the JWT without the "Bearer " prefix or null
     */
    private String parseJwt(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith(BEARER_)) {
            return null;
        }

        return header.substring(BEARER_.length());
    }
}

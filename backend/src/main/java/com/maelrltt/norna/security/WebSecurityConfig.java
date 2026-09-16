package com.maelrltt.norna.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    // Creates the JWT authentication filter
    // This filter is responsible for extracting and validating the JWT token from incoming requests
    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws AuthenticationException {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // This method configures how incoming HTTP requests are secured
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)

                // Configures the handler that is called when an unauthenticated user tries to access a protected resource
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(unauthorizedHandler)
                )

                // No HTTP session is created or used to store authentication data
                // Authentication is handled through the JWT token sent with requests
                .sessionManagement(s ->
                    s.sessionCreationPolicy( SessionCreationPolicy.STATELESS)
                )

                // Defines which endpoints require authentication
                .authorizeHttpRequests(a ->
                        a.requestMatchers("/auth/**").permitAll().anyRequest().authenticated()
                );

        // This allows the application to validate the JWT token and authenticate the user
        // before Spring Security checks whether authentication is required
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

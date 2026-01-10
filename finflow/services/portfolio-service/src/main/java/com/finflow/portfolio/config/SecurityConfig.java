package com.finflow.portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the portfolio service.
 * Provides PasswordEncoder bean for password hashing.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Provides BCrypt password encoder for hashing passwords.
     * BCrypt is a strong, adaptive hashing algorithm designed for passwords.
     *
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures HTTP security.
     * Currently permits all requests - authentication/authorization will be handled by API Gateway.
     *
     * @param http HttpSecurity configuration
     * @return SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless API
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Allow all requests (auth handled by API Gateway)
            );
        return http.build();
    }
}

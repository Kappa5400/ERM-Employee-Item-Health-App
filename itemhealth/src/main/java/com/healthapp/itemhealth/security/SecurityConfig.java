package com.healthapp.itemhealth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // This allows you to use @PreAuthorize on your Services
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // In a real SIer project, CSRF is usually enabled,
        // but for initial API testing, we disable it.
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            auth ->
                auth
                    // Allow anyone to VIEW items (Standard SIer "Read-Only" access)
                    .requestMatchers(HttpMethod.GET, "/api/**")
                    .permitAll()

                    // Everything else (POST, PUT, DELETE) requires login
                    .anyRequest()
                    .authenticated())
        // Use Basic Auth for easy testing in Postman/Browser
        .httpBasic(Customizer.withDefaults())
        // Also enable the standard login form
        .formLogin(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    // This is the SIer standard for password hashing
    return new BCryptPasswordEncoder();
  }
}

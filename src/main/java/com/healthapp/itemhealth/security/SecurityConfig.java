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
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // 1. Disable CSRF for development (Allows H2 and POST requests to work easily)
        .csrf(csrf -> csrf.disable())

        // 2. Configure Frame Options so the H2 Console can display in your browser
        .headers(headers -> headers.frameOptions(f -> f.sameOrigin()))

        // 3. Authorization Rules (ORDER MATTERS: Specific to General)
        .authorizeHttpRequests(
            auth ->
                auth
                    // Publicly accessible paths
                    .requestMatchers("/h2-console/**")
                    .permitAll()
                    .requestMatchers("/login", "/css/**", "/js/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/public/**")
                    .permitAll()

                    // Role-based paths
                    .requestMatchers("/api/employee/**")
                    .hasRole("BOSS")
                    .requestMatchers("/api/laptop/**")
                    .hasRole("BOSS")
                    .requestMatchers("/api/id-card/**")
                    .hasRole("BOSS")
                    .requestMatchers("/api/car/**")
                    .hasRole("BOSS")

                    // Everything else requires authentication
                    .anyRequest()
                    .authenticated())

        // 4. Custom Error Handling
        .exceptionHandling(exception -> exception.accessDeniedPage("/login"))

        // 5. Authentication Mechanisms
        .httpBasic(Customizer.withDefaults())
        .formLogin(form -> form.loginPage("/login"));

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}

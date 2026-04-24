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

  // The security filter chain
  // starts with  csrf, or cross-site request forgery security options 
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(Customizer.withDefaults())

       

        // authorization rules
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/login", "/css/**", "/js/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/public/**")
                    .permitAll()

                    // only boss can access below
                    .requestMatchers("/api/employee/**")
                    .hasRole("BOSS")
                    .requestMatchers("/api/laptop/**")
                    .hasRole("BOSS")
                    .requestMatchers("/api/id-card/**")
                    .hasRole("BOSS")
                    .requestMatchers("/api/car/**")
                    .hasRole("BOSS")

                    // authentication for everything else
                    .anyRequest()
                    .authenticated())

        // on exception have exception handler handle then redirect to login page
        .exceptionHandling(exception -> exception.accessDeniedPage("/login"))

        
        // must login to access rest of site, it attempt to access redirect to login page
        .formLogin(form -> form.loginPage("/login"));
    
    return http.build();
  }

  // Make bcrypt password encoder object.
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}

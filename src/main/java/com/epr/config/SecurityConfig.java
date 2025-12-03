// src/main/java/com/epr/config/SecurityConfig.java
package com.epr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Static resources
                        .requestMatchers("/", "/login", "/register",
                                "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                        // Swagger / OpenAPI docs
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html",
                                "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()

                        // === PUBLIC API ENDPOINTS - NO AUTH REQUIRED ===
                        .requestMatchers("/services/**").permitAll()           // This is what you need
                        .requestMatchers("/api/public/**").permitAll()         // if you have other public APIs later
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/blogs").permitAll()// optional - remove if you want to lock down other /api later

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // Disable CSRF only for APIs that are truly stateless or used by external clients
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**", "/services/**","/blogs")   // Add /services here too if needed
                );

        return http.build();
    }
}
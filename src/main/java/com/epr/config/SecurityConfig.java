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
                        // Static resources & basic pages
                        .requestMatchers("/", "/login", "/register",
                                "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                        // Swagger / OpenAPI
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html",
                                "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()

                        // === PUBLIC API ENDPOINTS - NO AUTH REQUIRED ===
                        .requestMatchers("/services/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/client/**").permitAll()

                        // === BLOG ENDPOINTS - FULLY PUBLIC ===
                        .requestMatchers("/blogs", "/blogs/**").permitAll()

                        // Everything else requires authentication (admin pages, etc.)
                        .anyRequest().authenticated()
                )

                // ADD THIS: Prevents 302 redirects on API calls
                .httpBasic(httpBasic -> httpBasic
                        .realmName("EPR Application")  // Optional, but nice to have
                )

                // Keep form login for browser-based web flows
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                // Disable CSRF for public/stateless APIs
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/api/**",
                                "/services/**",
                                "/blogs", "/blogs/**",
                                "/client/**"  // Recommended: add this too for safety
                        )
                );

        return http.build();
    }
}
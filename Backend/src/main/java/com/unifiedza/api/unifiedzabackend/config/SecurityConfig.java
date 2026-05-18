package com.unifiedza.api.unifiedzabackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)

                //Route permissions config
                .authorizeHttpRequests(auth -> auth
                    //Endpoints access to frontend without authentication
                        .requestMatchers("/api/auth/**", "/api/faq/**").permitAll()

                        //Rest of the endpoints require authentication
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}

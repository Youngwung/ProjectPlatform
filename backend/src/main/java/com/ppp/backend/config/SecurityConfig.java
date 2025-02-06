package com.ppp.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors() // ✅ CorsFilter 적용됨
                .and()
                .csrf().disable() // CSRF 비활성화 (주의 필요)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // API는 누구나 접근 가능
                        .anyRequest().authenticated() // 나머지는 인증 필요
                );

        return http.build();
    }
}

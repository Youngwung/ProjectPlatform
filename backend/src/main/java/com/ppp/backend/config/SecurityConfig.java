package com.ppp.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ppp.backend.handler.CustomAuthenticationsSuccessHandler;
import com.ppp.backend.security.JwtAuthenticationFilter;
import com.ppp.backend.service.CustomOAuth2UserService;
import com.ppp.backend.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationsSuccessHandler customAuthenticationsSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // ✅ BCrypt 방식의 패스워드 인코딩 사용
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ CORS 설정 활성화
                .csrf(csrf -> csrf.disable()) // ✅ CSRF 보호 비활성화 (쿠키 인증 시 주의 필요) TODO 나중에 주석처리 할수도?
                .authorizeHttpRequests(auth -> auth
                        // TODO 필요한 요청은 auth로 처리해야함 다 뚫어놓음
                        .requestMatchers("/api/auth/*").permitAll() // ✅ 로그인 & 회원가입은 인증 필요 없음

                        .requestMatchers("/api/portfolio/create").authenticated() // ✅ 포트폴리오 등록(POST) 인증 필요
                        .requestMatchers("/api/portfolio/edit").authenticated() // ✅ 포트폴리오 수정(PUT) 인증 필요
                        .requestMatchers("/api/portfolio/delete").authenticated() // ✅ 포트폴리오 삭제(DELETE) 인증 필요
                        // TODO project 요청처리해야함
                        .requestMatchers(HttpMethod.POST, "/api/project/*").authenticated() // ✅ 프로젝트 등록(POST) 인증 필요
                        .requestMatchers(HttpMethod.PUT, "/api/project/*").authenticated() // ✅ 프로젝트 수정(PUT) 인증 필요
                        .requestMatchers(HttpMethod.DELETE, "/api/project/*").authenticated() // ✅ 프로젝트 삭제(DELETE) 인증 필요
                        .requestMatchers(HttpMethod.PUT, "/api/user/list/*").authenticated() // ✅ 프로젝트 수정(PUT) 인증 필요
                        .requestMatchers(HttpMethod.DELETE, "/api/user/list/*").authenticated() // ✅ 프로젝트 삭제(DELETE) 인증
                                                                                                // 필요
                        .anyRequest().permitAll() // ✅ 나머지 모든 요청은 허용
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ 세션을
                .oauth2Login(oauth2Login -> {
                    oauth2Login
                            .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2UserService))
                            .successHandler(customAuthenticationsSuccessHandler);
                })
                .logout(logout -> {
                    logout.logoutSuccessUrl("http://localhost:3000/");
                })
                                                                                                              // 사용하지
                                                                                                              // 않도록 설정
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // ✅ 클라이언트 도메인 지정
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ 허용할 HTTP 메서드 지정
        configuration.setAllowedHeaders(List.of("*")); // ✅ 모든 헤더 허용
        configuration.setAllowCredentials(true); // ✅ 쿠키 인증 허용 (필수)
        configuration.setExposedHeaders(List.of("Authorization")); // ✅ 프론트에서 Authorization 헤더 접근 가능

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}

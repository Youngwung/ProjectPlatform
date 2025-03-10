package com.ppp.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ 프론트엔드 도메인 명시 (withCredentials 사용 시 * 불가)
        config.setAllowedOrigins(List.of("http://localhost:3000"));

        // ✅ 허용할 HTTP 메서드 지정
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ 모든 헤더 허용
        config.setAllowedHeaders(List.of("*"));

        // ✅ 인증 정보 포함 (JWT 사용 시 필수)
        config.setAllowCredentials(true);

        // ✅ HTTP 응답 헤더 설정 (프록시 요청 방지)
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}

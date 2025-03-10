package com.ppp.backend.util;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthUtil {

    // JwtUtil 의존성: JWT 토큰의 검증 및 파싱 기능 제공
    private final JwtUtil jwtUtil;

    public Long extractUserIdFromCookie(HttpServletRequest request) {
        // 요청에 포함된 모든 쿠키 배열을 가져옵니다.
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            // 각 쿠키를 순회하면서 "accessToken" 쿠키를 찾습니다.
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    String token = cookie.getValue(); // JWT 토큰 값 추출
                    try {
                        // 토큰 유효성 검사: 유효한 토큰이면 true 반환, 아니면 예외 발생
                        if (jwtUtil.validateToken(token)) {
                            // 유효한 토큰에서 사용자 ID를 추출하여 반환
                            return jwtUtil.extractUserId(token);
                        }
                    } catch (Exception e) {
                        // 토큰 검증 실패 시 경고 로그를 남기고 null 반환
                        log.warn("🚨 JWT 검증 실패: {}", e.getMessage());
                        return null;
                    }
                }
            }
        }
        // 쿠키가 없거나 accessToken 쿠키를 찾지 못한 경우 경고 로그 출력 후 null 반환
        log.warn("🚨 인증되지 않은 사용자 요청 (JWT 없음)");
        return null;
    }

    public String extractEmailFromCookie(HttpServletRequest request) {
        // 요청에 포함된 모든 쿠키 배열을 가져옵니다.
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            // 각 쿠키를 순회하면서 "accessToken" 쿠키를 찾습니다.
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    String token = cookie.getValue(); // JWT 토큰 값 추출
                    try {
                        // 토큰 유효성 검사: 유효한 토큰이면 true 반환, 아니면 예외 발생
                        if (jwtUtil.validateToken(token)) {
                            // 유효한 토큰에서 사용자 ID를 추출하여 반환
                            return jwtUtil.extractEmail(token);
                        }
                    } catch (Exception e) {
                        // 토큰 검증 실패 시 경고 로그를 남기고 null 반환
                        log.warn("🚨 JWT 검증 실패: {}", e.getMessage());
                        return null;
                    }
                }
            }
        }
        // 쿠키가 없거나 accessToken 쿠키를 찾지 못한 경우 경고 로그 출력 후 null 반환
        log.warn("🚨 인증되지 않은 사용자 요청 (JWT 없음)");
        return null;
    }
}

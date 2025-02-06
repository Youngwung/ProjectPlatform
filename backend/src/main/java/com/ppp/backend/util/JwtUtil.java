package com.ppp.backend.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // ✅ 비밀 키를 환경 변수에서 가져옴 (`application.properties`에서 설정)
    @Value("${jwt.secret}")
    private String secretKey;

    // ✅ JWT 만료 시간 (1시간)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    // ✅ 비밀키를 Key 객체로 변환 (최신 방식 적용)
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 🔹 **JWT 토큰 생성**
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date()) // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // ✅ 최신 signWith 방식 적용
                .compact();
    }

    // 🔹 **토큰에서 사용자 정보(이메일 또는 ID) 추출**
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // 🔹 **토큰이 유효한지 검사**
    public boolean validateToken(String token) {
        try {
            return extractUsername(token) != null && !isTokenExpired(token);
        } catch (Exception e) {
            return false; // 예외 발생 시 유효하지 않은 토큰 처리
        }
    }

    // 🔹 **토큰이 만료되었는지 확인**
    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // 🔹 **토큰에서 Claims(정보) 추출**
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

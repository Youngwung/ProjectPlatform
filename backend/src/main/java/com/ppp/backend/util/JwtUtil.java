package com.ppp.backend.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    // ✅ 서명 키 생성
    private Key getSigningKey() {
        byte[] keyBytes;

        // ✅ Base64 여부 확인 후 디코딩
        if (isBase64Encoded(secretKey)) {
            keyBytes = Base64.getDecoder().decode(secretKey);
        } else {
            keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        }

        // ✅ 키 길이 검증 (최소 32바이트 필요)
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT Secret Key must be at least 256 bits (32 bytes)!");
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }
    private boolean isBase64Encoded(String key) {
        try {
            byte[] decoded = Base64.getDecoder().decode(key);
            return key.equals(Base64.getEncoder().encodeToString(decoded));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // ✅ JWT 생성 (userId & email 저장)
    public String generateToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // `userId`를 subject로 저장
                .claim("email", email) // `email`을 추가 claims에 저장
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ✅ JWT에서 userId 추출
    public Long extractUserId(String token) {
        return Long.valueOf(parseClaims(token).getSubject());
    }

    // ✅ JWT에서 email 추출
    public String extractEmail(String token) {
        return parseClaims(token).get("email", String.class);
    }

    // ✅ 공통 Claims 파싱 메서드
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 소셜로그인 회원가입 시 가져온 정보를 채우기 위한 임시 토큰 발급 메서드
    public String generateTempToken(String email, String name, String providerName) {
        return Jwts.builder()
                .setSubject(email) // email을 subject로 저장
                .claim("name", name) // `email`을 추가 claims에 저장
                .claim("providerName", providerName)
                .claim("temp", true) // 임시 토큰임을 표시
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 임시 토큰 여부 확인 메서드
    public boolean isTemporaryToken(String token) {
        return parseClaims(token).containsKey("temp") && (boolean) parseClaims(token).get("temp");
    }
}

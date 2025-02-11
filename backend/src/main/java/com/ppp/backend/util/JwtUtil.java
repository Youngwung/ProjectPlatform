package com.ppp.backend.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Base64;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    private boolean isBase64Encoded(String key) {
        try {
            byte[] decoded = java.util.Base64.getDecoder().decode(key);
            return key.equals(java.util.Base64.getEncoder().encodeToString(decoded));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // ✅ 서명 키 생성 (Base64 인코딩 적용)
    private Key getSigningKey() {
        byte[] keyBytes;

        if (isBase64Encoded(secretKey)) { // Base64 체크
            keyBytes = Base64.getDecoder().decode(secretKey);
        } else {
            keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        }

        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT Secret Key must be at least 256 bits (32 bytes)!");
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }


    // ✅ JWT 생성 (userId & email 저장)
    public String generateToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // ✅ sub에 userId 저장
                .claim("email", email) // ✅ 이메일 추가 저장
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
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.getSubject()); // ✅ userId 반환
    }

    // ✅ JWT에서 이메일(Username) 추출
    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("email", String.class); // ✅ JWT claims에서 email 반환
    }
}

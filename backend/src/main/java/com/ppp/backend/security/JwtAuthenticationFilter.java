package com.ppp.backend.security;

import com.ppp.backend.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public JwtAuthenticationFilter(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("✅ JwtAuthenticationFilter 실행됨");
        String token = getJwtFromRequest(request);
        if (token == null) {
            System.out.println("❌ JWT가 없음! (401 Unauthorized 발생)");
        } else {
            System.out.println("✅ 찾은 JWT: " + token);
        }
        log.info("JWT token1231: {}", token);

        if (token != null) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody();
                System.out.println("✅ JWT 파싱 성공 - 사용자: " + claims.getSubject());

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(claims.getSubject());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                System.out.println("JWT 인증 실패: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        System.out.println("✅ getJwtFromRequest 실행됨");

        // 1️⃣ Authorization 헤더에서 JWT 가져오기
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            System.out.println("✅ Authorization 헤더에서 JWT 찾음: " + token.substring(7));
            return token.substring(7);
        }

        // 2️⃣ 쿠키에서 JWT 가져오기 (`accessToken` 또는 `jwt` 쿠키 찾기)
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                System.out.println("✅ 쿠키 확인 - 이름: " + cookie.getName() + ", 값: " + cookie.getValue());

                if ("accessToken".equals(cookie.getName()) || "jwt".equals(cookie.getName())) {
                    System.out.println("✅ 쿠키에서 JWT 찾음: " + cookie.getValue());
                    return cookie.getValue();
                }
            }
        }

        System.out.println("❌ JWT를 찾을 수 없음!");
        return null; // JWT가 없을 경우 null 반환
    }

}

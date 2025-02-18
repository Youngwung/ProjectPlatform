package com.ppp.backend.security;

import com.ppp.backend.service.CustomUserDetailsService;
import com.ppp.backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * ✅ OncePerRequestFilter의 doFilterInternal()을 오버라이드
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("✅ [JwtAuthenticationFilter] 실행됨 - 요청 URL: {}", request.getRequestURI());

        String token = getJwtFromRequest(request);

        if (token == null) {
            log.warn("🚨 [JwtAuthenticationFilter] JWT가 요청에서 감지되지 않음 (쿠키 또는 헤더 확인 필요)");
        } else {
            log.info("🔹 [JwtAuthenticationFilter] 찾은 JWT: {}", token);
        }

        if (token != null && jwtUtil.validateToken(token)) {
            try {
                Long userId = jwtUtil.extractUserId(token);
                String email = jwtUtil.extractEmail(token);

                log.info("✅ [JwtAuthenticationFilter] JWT 파싱 성공 - userId: {}, email: {}", userId, email);

                // ✅ userId 기반 사용자 조회 (없으면 email로 조회)
                UserDetails userDetails = customUserDetailsService.loadUserByUserIdOrEmail(userId, email);

                if (userDetails == null) {
                    log.warn("🚨 [JwtAuthenticationFilter] 사용자 정보 조회 실패 - userId: {}, email: {}", userId, email);
                } else {
                    log.info("✅ [JwtAuthenticationFilter] 사용자 인증 성공 - userId: {}, 권한: {}", userId, userDetails.getAuthorities());

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("🔹 [JwtAuthenticationFilter] SecurityContext에 사용자 인증 정보 설정 완료.");
                }
            } catch (Exception e) {
                log.error("🚨 [JwtAuthenticationFilter] JWT 인증 실패: {}", e.getMessage(), e);
            }
        } else {
            log.warn("🚨 [JwtAuthenticationFilter] JWT가 유효하지 않거나 만료됨.");
        }

        filterChain.doFilter(request, response);
    }

    /**
     * ✅ JWT를 가져오는 로직을 일원화 (헤더 및 쿠키에서 검색)
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        log.info("🔍 [JwtAuthenticationFilter] JWT 검색 시작");

        // ✅ 1. Authorization 헤더에서 JWT 가져오기
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            log.info("✅ [JwtAuthenticationFilter] Authorization 헤더에서 JWT 찾음: {}", token.substring(7));
            return token.substring(7);
        }

        // ✅ 2. 쿠키에서 JWT 가져오기
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                log.info("🔹 [JwtAuthenticationFilter] 쿠키 확인 - 이름: {}, 값: {}", cookie.getName(), cookie.getValue());

                if ("accessToken".equals(cookie.getName()) || "jwt".equals(cookie.getName())) {
                    log.info("✅ [JwtAuthenticationFilter] 쿠키에서 JWT 찾음: {}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }

        log.warn("🚨 [JwtAuthenticationFilter] JWT를 찾을 수 없음!");
        return null;
    }
}

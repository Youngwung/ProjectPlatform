package com.ppp.backend.controller;

import com.ppp.backend.dto.UserDto;
import com.ppp.backend.service.UserService;
import com.ppp.backend.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthApiController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * ✅ 로그인 API
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto loginRequest, HttpServletResponse response) {
        log.info("🔑 로그인 요청: {}", loginRequest.getEmail());

        String username = userService.findUserNameByEmail(loginRequest.getEmail());
        if (username == null || !userService.login(loginRequest.getEmail(), loginRequest.getPassword())) {
            return ResponseEntity.status(401).body("❌ 로그인 실패: 이메일 또는 비밀번호가 잘못되었습니다.");
        }

        // ✅ JWT 생성 (userId & email 포함)
        Long userId = userService.findByEmail(loginRequest.getEmail()).getId();
        String jwt = jwtUtil.generateToken(userId, loginRequest.getEmail());

        // ✅ JWT를 HttpOnly 쿠키로 설정
        Cookie jwtCookie = new Cookie("accessToken", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60 * 24); // 1일 유지
        response.addCookie(jwtCookie);
        log.info("✅ 로그인 성공: {} (JWT 발급 완료)", username);

        return ResponseEntity.ok("로그인 성공");
    }

    /**
     * ✅ 로그아웃 API (쿠키 삭제)
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("accessToken", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // 즉시 만료

        response.addCookie(jwtCookie);
        log.info("✅ 로그아웃 성공 (JWT 쿠키 삭제)");

        return ResponseEntity.ok("로그아웃 성공");
    }

    /**
     * ✅ 현재 로그인된 사용자 정보 반환
     */
    @GetMapping("/getAuthenticatedUser")
    public ResponseEntity<?> getAuthenticatedUser(HttpServletRequest request) {
        // ✅ 쿠키에서 JWT 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (jwtUtil.validateToken(token)) {
                        Long userId = jwtUtil.extractUserId(token);
                        String email = jwtUtil.extractUsername(token);
                        log.info("🔎 사용자 정보 조회 성공: userId={}, email={}", userId, email);

                        // ✅ 사용자 정보 조회
                        UserDto userDto = userService.getUserById(userId);
                        return ResponseEntity.ok(userDto);
                    }
                }
            }
        }

        log.warn("🚨 인증된 사용자 없음 (쿠키에 유효한 JWT 없음)");
        return ResponseEntity.status(401).body("인증되지 않은 사용자");
    }
}

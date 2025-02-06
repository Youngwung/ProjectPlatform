package com.ppp.backend.controller;

import com.ppp.backend.service.JwtBlacklistService;
import com.ppp.backend.util.JwtUtil;
import com.ppp.backend.service.UserService;
import com.ppp.backend.dto.UserDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final JwtBlacklistService jwtBlacklistService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto loginRequest, HttpServletResponse response) {
        boolean isValidUser = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (!isValidUser) {
            return ResponseEntity.status(401).body("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        // JWT 생성
        String token = jwtUtil.generateToken(loginRequest.getEmail());

        // JWT를 httpOnly 쿠키에 저장
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);  // HTTPS 환경에서는 true로 변경 필요
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok("로그인 성공");
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);

        // 현재 JWT를 블랙리스트 처리할 수 있도록 추가적으로 관리 가능
        String token = extractTokenFromRequest(request);
        if (token != null) {
            jwtBlacklistService.addToBlacklist(token); // 블랙리스트에 추가
        }
        return ResponseEntity.ok("로그아웃 성공");
    }

    // 요청 헤더에서 토큰 추출
    private String extractTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    // 인증된 사용자 조회
    @GetMapping("/getAuthenticatedUser")
    public ResponseEntity<?> getUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (jwtUtil.validateToken(token)) {
                        String username = jwtUtil.extractUsername(token);
                        return ResponseEntity.ok(username);
                    }
                }
            }
        }
        return ResponseEntity.status(401).body("Unauthorized");
    }
}

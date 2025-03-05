package com.ppp.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import com.ppp.backend.dto.UserDto;
import com.ppp.backend.service.UserService;
import com.ppp.backend.util.AuthUtil;
import com.ppp.backend.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthApiController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthUtil authUtil;
    /**
     * ✅ 로그인 API
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto loginRequest, HttpServletResponse response) {
        log.info("🔑 로그인 요청: {}", loginRequest.getEmail());

        // ✅ 로그인 검증 수행
        ResponseEntity<Map<String, Object>> loginResponse = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        // 로그인 실패 시 클라이언트에 실패 응답 반환
        if (loginResponse.getStatusCode() != HttpStatus.OK) {
            return loginResponse;
        }

        // ✅ 로그인 성공 시 JWT 발급 및 쿠키 설정
        Map<String, Object> responseBody = loginResponse.getBody();
        String jwt = (String) responseBody.get("accessToken");
        Long userId = (Long) responseBody.get("userId");

        Cookie jwtCookie = new Cookie("accessToken", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // 개발 환경에서는 false, 운영 환경에서는 true 설정 필요
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60 * 24); // 1일 유지
        response.addCookie(jwtCookie);

        log.info("✅ 로그인 성공: userId={}, email={}", userId, loginRequest.getEmail());
        return ResponseEntity.ok(responseBody);
    }

    /**
     * ✅ 이메일 중복 확인 API
     */
    @PostMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestBody UserDto userDto) {
        log.info("📧 이메일 중복 확인 요청: {}", userDto.getEmail());

        // ✅ 이메일이 null이거나 공백인지 확인
        if (userDto.getEmail() == null || userDto.getEmail().trim().isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", true);
            errorResponse.put("message", "이메일이 비어있습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        boolean exists = userService.isEmailExists(userDto.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("message", exists ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다.");
        log.info("{} res",response);
        return ResponseEntity.ok(response);
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
        Long userId = authUtil.extractUserIdFromCookie(request);
        if (userId == null) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자");
        }

        UserDto userDto = userService.getUserById(userId);
        log.info("🔎 사용자 정보 조회 성공: userId={}, name={}, dto={}", userDto.getId(), userDto.getName(), userDto);
        return ResponseEntity.ok(userDto);
    }
    /**
     * ✅ 사용자 정보 수정 API
     */
    @PutMapping("/updateuser")
    public ResponseEntity<?> updateUserInfo(@RequestBody UserDto updatedUser, HttpServletRequest request) {
        System.out.println("업데이트 유저정보" + updatedUser);
        Long userId = authUtil.extractUserIdFromCookie(request);
        if (userId == null) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자");
        }
        try {
            UserDto updatedUserInfo = userService.updateUserInfo(userId, updatedUser);
            log.info("✅ 사용자 정보 수정 완료: userId={}, {}", userId, updatedUser);
            return ResponseEntity.ok(updatedUserInfo);
        } catch (Exception e) {
            log.error("❌ 사용자 정보 수정 실패: userId={}, 오류={}", userId, e.getMessage());
            return ResponseEntity.status(500).body("사용자 정보 수정 중 오류가 발생했습니다.");
        }
    }
    @PutMapping("/updatedExperience")
    public ResponseEntity<?> updateExperience(
            @RequestBody UserDto updatedUserExperience, // ✅ `id` 제거
            HttpServletRequest request) {

        log.info("🔄 사용자 경험치 업데이트 요청: experience={}", updatedUserExperience.getExperience());

        // 🔹 1. JWT 쿠키에서 사용자 ID 추출
        Long userId = authUtil.extractUserIdFromCookie(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "인증되지 않은 사용자입니다."));
        }

        // 🔹 2. 경험치 업데이트
        UserDto updatedUser = userService.updateUserExperience(userId, updatedUserExperience.getExperience()); // ✅ `UserDto` 대신 `experience` 값만 전달
        log.info("✅ 경험치 업데이트 완료: userId={}, newExperience={}", userId, updatedUser.getExperience());

        // 🔹 3. 업데이트된 사용자 정보 반환
        return ResponseEntity.ok(updatedUser);
    }
    /**
     * ✅ 비밀번호 검증 API
     * 클라이언트에서 현재 비밀번호를 전송하면, 해당 비밀번호가 맞는지 검증합니다.
     */
    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody UserDto passwordRequest, HttpServletRequest request) {
        // JWT 쿠키에서 userId 추출
        Long userId = authUtil.extractUserIdFromCookie(request);
        if (userId == null) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자");
        }
        try {
            // 전달받은 UserDto의 password 필드를 현재 비밀번호로 사용하여 검증
            boolean isValid = userService.verifyPassword(userId, passwordRequest.getPassword());
            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            // 에러 발생 시, 비밀번호 값이 null인지 여부 등 자세한 로그 기록 (비밀번호 원문은 민감 정보이므로 주의)
            log.error("❌ 비밀번호 검증 실패: userId={}, 오류={}, passwordProvided={}",
                    userId, e.getMessage(), passwordRequest.getPassword() != null ? "YES" : "NO");
            return ResponseEntity.status(500).body("비밀번호 검증 중 오류가 발생했습니다.");
        }
    }

    /**
     * ✅ 비밀번호 변경 API
     * 클라이언트에서 현재 비밀번호와 새 비밀번호를 보내면, JWT 쿠키에서 userId를 추출하여 비밀번호를 변경합니다.
     */
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody UserDto passwordRequest, HttpServletRequest request) {
        Long userId = authUtil.extractUserIdFromCookie(request);
        if (userId == null) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자");
        }
        try {
            // passwordRequest.getPassword() -> 현재 비밀번호
            // passwordRequest.getNewPassword() -> 새 비밀번호
            userService.updatePassword(userId, passwordRequest.getPassword(), passwordRequest.getNewPassword());
            log.info("✅ 비밀번호 변경 완료: userId={} (현재 비밀번호: {}, 새 비밀번호: {})",
                    userId, passwordRequest.getPassword(), passwordRequest.getNewPassword());
            return ResponseEntity.ok("비밀번호 변경 성공");
        } catch (Exception e) {
            log.error("❌ 비밀번호 변경 실패: userId={}, 오류={}", userId, e.getMessage());
            return ResponseEntity.status(500).body("비밀번호 변경 중 오류가 발생했습니다.");
        }
    }

    
    @GetMapping("/oauth2")
    public Map<String, Object> oauth2User(@AuthenticationPrincipal OAuth2User principal) {
        log.info("OAuth2 = {}", principal.getAttributes());
        return principal.getAttributes();
    }

    @DeleteMapping("/deleteuser")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {

        // JWT 쿠키에서 userId 추출하여 본인 확인
        Long userId = authUtil.extractUserIdFromCookie(request);
        if (userId == null) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자");
        }

        try {
            userService.deleteUser(userId);
            log.info("✅ 회원 탈퇴 완료: userId={}", userId);
            return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
        } catch (Exception e) {
            log.error("❌ 회원 탈퇴 실패: userId={}, 오류={}", userId, e.getMessage());
            return ResponseEntity.status(500).body("회원 탈퇴 중 오류가 발생했습니다.");
        }
    }

}

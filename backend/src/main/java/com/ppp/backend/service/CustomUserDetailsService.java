package com.ppp.backend.service;

import com.ppp.backend.domain.User;
import com.ppp.backend.repository.UserRepository;
import com.ppp.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService { // ✅ 반드시 UserDetailsService 구현

    private final UserRepository userRepository;

    /**
     * ✅ 기본 `loadUserByUsername()` 구현 (Spring Security에서 필수)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("🔍 사용자 정보 조회 (이메일): {}", email);
        return userRepository.findByEmail(email)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));
    }

    /**
     * ✅ 사용자 ID 또는 이메일 기반 조회 (직접 호출하는 메서드)
     */
    public UserDetails loadUserByUserIdOrEmail(Long userId, String email) {
        log.info("🔍 사용자 정보 조회 - userId: {}, email: {}", userId, email);
        return userRepository.findById(userId)
                .or(() -> userRepository.findByEmail(email))
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}

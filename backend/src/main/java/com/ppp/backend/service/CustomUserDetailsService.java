package com.ppp.backend.security;

import com.ppp.backend.domain.User;
import com.ppp.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // ✅ Bean으로 등록
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);
        }
        User user = userOptional.get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()) // ✅ username을 이메일로 설정
                .password(user.getPassword()) // ✅ 비밀번호 설정
                .roles("USER") // ✅ 기본 역할 설정
                .build();
    }
}

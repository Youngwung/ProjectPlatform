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

import java.util.Optional;

@Service // ✅ Bean으로 등록
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findById(Long.parseLong(id));
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + id);
        }
        System.out.println("ㅅㄳㄱ");
        User user = userOptional.get();
        return new CustomUserDetails(user);
    }
}

package com.ppp.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.ppp.backend.domain.User;
import com.ppp.backend.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

// @SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // @Test
    public void createUser() {

        User user = new User();
        user.toBuilder()
                .email("testEmail")
                .name("testname")
                .password("1234")
                .phoneNumber("123")
                .experience("123")
                .build();
        userRepository.save(user);
    }
}

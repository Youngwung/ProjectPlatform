package com.ppp.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

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

  
}

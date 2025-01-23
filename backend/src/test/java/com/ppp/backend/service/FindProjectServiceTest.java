package com.ppp.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class FindProjectServiceTest {
    @Autowired
    private FindProjectService fPService;

    @Test
    public void getOneTest() {
        Long fPNo = 1L;

        log.info("get = {}", fPService.getFindProjectById(fPNo));
    }
    @Test
    public void getAllTest() {
        log.info("all={}",fPService.getAllFindProjects());
    }
}

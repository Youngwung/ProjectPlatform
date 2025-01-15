package com.ppp.backend.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class TestRepositoryTest {
    @Autowired
    private TestRepository testRepository;

    @Test
    public void testAll() {
    }
}
package com.ppp.backend.repository;

import com.ppp.backend.entity.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@SpringBootTest
public class TestRepositoryTest {
    @Autowired
    private TestRepository testRepository;

    @org.junit.jupiter.api.Test
    public void testInsert() {
        testRepository.save(Test.builder().id(1L).name("이정인").build());
    }

}
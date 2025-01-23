package com.ppp.backend.repository;

import com.ppp.backend.domain.FindProject;
import com.ppp.backend.domain.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@ActiveProfiles("local")
@SpringBootTest
@Slf4j
public class FindProjectRepositoryTest {

    @Autowired
    private FindProjectRepository findProjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findProjectRepositoryNotNull() {
        Assertions.assertNotNull(findProjectRepository);
        log.info("FindProjectRepository initialized: {}", findProjectRepository);
    }

    @Test
    @Transactional
    public void insertFindProject() {
        // ID가 1인 User 조회
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found with ID 1"));

        // FindProject 생성
        FindProject findProject = FindProject.builder()
                .user(user)
                .title("Find Project Title")
                .description("This is a test project.")
                .build();

        findProjectRepository.save(findProject);
        log.info("Inserted FindProject: {}", findProject);
    }

    @Test
    @Transactional
    public void updateFindProject() {
        // 기존 FindProject 조회
        FindProject findProject = findProjectRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("FindProject not found with ID 1"));

        // FindProject 정보 수정
        findProject = findProject.toBuilder()
                .title("Updated Project Title")
                .description("Updated description for the project.")
                .build();

        findProjectRepository.save(findProject);
        log.info("Updated FindProject: {}", findProject);
    }

    @Test
    @Transactional
    public void deleteFindProject() {
        // 기존 FindProject 삭제
        Long findProjectId = 1L;
        findProjectRepository.deleteById(findProjectId);
        log.info("Deleted FindProject with ID: {}", findProjectId);

        // 삭제 확인
        Optional<FindProject> deletedProject = findProjectRepository.findById(findProjectId);
        Assertions.assertTrue(deletedProject.isEmpty());
    }
}

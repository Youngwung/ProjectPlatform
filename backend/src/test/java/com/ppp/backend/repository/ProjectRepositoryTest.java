package com.ppp.backend.repository;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ppp.backend.domain.Project;
import com.ppp.backend.domain.User;
import com.ppp.backend.status.ProjectStatus;

import lombok.extern.slf4j.Slf4j;

// @SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class ProjectRepositoryTest {
	@Autowired
	private ProjectRepository projectRepo;
	@Autowired
	private UserRepository userRepo;

	// @Test
	public void testDi() {
		Assertions.assertNotNull(projectRepo);
		log.info("projectRepo = {}", projectRepo);
	}

	// @Test
	public void testInsert() {
		User user = userRepo.findById(1L).orElseThrow();
		Project joinProject = Project.builder()
				.title("DefaultTest2")
				.user(user).build();
		projectRepo.save(joinProject);
	}

	// @Test
	@Transactional
	public void readTest() {
		Project joinProject = projectRepo.findById(1L).orElseThrow(null);
		log.info("joinProject = {} ", joinProject);
	}

	// @Test
	public void updateTest() {
		User user = userRepo.findById(1L).orElseThrow(null);
		Project jp = Project.builder().id(1L).title("DefaultUpdateTest1").status(ProjectStatus.모집_중).user(user).build();
		projectRepo.save(jp);
	}

	// @Test
	@Transactional
	public void pagingTest() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

		Page<Project> result = projectRepo.findAll(pageable);

		// 전체 원소 개수
		log.info("resultTotalElements = {}", result.getTotalElements());

		// 전체 페이지 개수
		log.info("resultTotalPage = {}", result.getTotalPages());

		// page 처리가 된 리스트
		log.info("result = {}", result.getContent());

	}
}

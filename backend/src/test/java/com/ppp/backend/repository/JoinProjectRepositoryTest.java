package com.ppp.backend.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ppp.backend.domain.JoinProject;
import com.ppp.backend.domain.User;
import com.ppp.backend.status.JoinProjectStatus;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class JoinProjectRepositoryTest {
	@Autowired
	private JoinProjectRepository jPRepo;
	@Autowired
	private UserRepository userRepo;

	@Test
	public void testDi() {
		Assertions.assertNotNull(jPRepo);
		log.info("jPRepo = {}", jPRepo);
	}

	@Test
	public void testInsert() {
		User user = userRepo.findById(1L).orElseThrow();
		JoinProject joinProject = JoinProject.builder()
				.title("DefaultTest2")
				.user(user).build();
		jPRepo.save(joinProject);
	}

	@Test
	@Transactional
	public void readTest() {
		JoinProject joinProject = jPRepo.findById(1L).orElseThrow(null);
		log.info("joinProject = {} ", joinProject);
	}

	@Test
	public void updateTest() {
		User user = userRepo.findById(1L).orElseThrow(null);
		JoinProject jp = JoinProject.builder().id(1L).title("DefaultUpdateTest1").status(JoinProjectStatus.모집_중).user(user).build();
		jPRepo.save(jp);
	}

	@Test
	@Transactional
	public void pagingTest() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

		Page<JoinProject> result = jPRepo.findAll(pageable);

		// 전체 원소 개수
		log.info("resultTotalElements = {}", result.getTotalElements());

		// 전체 페이지 개수
		log.info("resultTotalPage = {}", result.getTotalPages());

		// page 처리가 된 리스트
		log.info("result = {}", result.getContent());

	}
}

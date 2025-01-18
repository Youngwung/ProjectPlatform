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

import com.ppp.backend.domain.Joinproject;
import com.ppp.backend.domain.User;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class JoinProjectRepositoryTest {
	@Autowired
	private JoinprojectRepository jpRepo;
	@Autowired
	private UserRepository userRepo;

	@Test
	public void testDi() {
		Assertions.assertNotNull(jpRepo);
		log.info("jpRepo = {}", jpRepo);
	}

	// @Test
	public void testInsert() {
		User user = userRepo.findById(1L).orElseThrow();
		Joinproject joinProject = Joinproject.builder()
				.title("TestTitle")
				.user(user).build();
		jpRepo.save(joinProject);
	}

	@Test
	public void readTest() {
		Joinproject joinproject = jpRepo.findById(1L).orElseThrow(null);
		log.info("joinproject = {} ", joinproject);
	}

	// @Test
	public void updateTest() {
		User user = userRepo.findById(1L).orElseThrow(null);
		Joinproject jp = Joinproject.builder().id(1L).title("updatetest").user(user).build();
		jpRepo.save(jp);
	}

	@Test
	@Transactional
	public void pagingTest() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

		Page<Joinproject> result = jpRepo.findAll(pageable);

		// 전체 원소 개수
		log.info("resultTotalElements = {}", result.getTotalElements());

		// 전체 페이지 개수
		log.info("resultTotalPage = {}", result.getTotalPages());

		// page 처리가 된 리스트
		log.info("result = {}", result.getContent());

	}
}

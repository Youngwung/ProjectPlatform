package com.ppp.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

// @SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class ProjectSearchTest {
	
	@Autowired
	private ProjectRepository projectRepo;


	
	// @Test
	@Transactional
	public void search1Test() {
		projectRepo.search1(null);
	}
	
}

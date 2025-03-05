package com.ppp.backend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class ProjectApplicationRepositoryTest {
	@Autowired
	private ProjectApplicationRepository projectApplicationRepo;

	@Test
	public void diTest() {
		log.info("projectApplicationRepo = {}", projectApplicationRepo);
	}
}

package com.ppp.backend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles("local")
@Slf4j
public class ProjectMemberRepositoryTest {
	@Autowired
	private ProjectMemberRepository projectMemberRepo;

	@Test
	public void diTest() {
		log.info("projectMemberRepo = {}", projectMemberRepo);
	}
}

package com.ppp.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class JoinProjectSearchTest {
	
	@Autowired
	private JoinProjectRepository jPRepo;


	
	// @Test
	@Transactional
	public void search1Test() {
		jPRepo.search1(null);
	}
	
}

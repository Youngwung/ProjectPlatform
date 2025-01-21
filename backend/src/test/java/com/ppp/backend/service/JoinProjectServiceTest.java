package com.ppp.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ppp.backend.dto.JoinProjectDTO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class JoinProjectServiceTest {
	@Autowired
	private JoinProjectService jPService;

	@Test
	public void getTest() {
		Long jPNo = 1L;

		log.info("get = {}", jPService.get(jPNo));
	}

	@Test
	public void registerTest() {
		JoinProjectDTO dto =JoinProjectDTO.builder()
		.title("registerTest")
		.description("testDescription")
		.userId(1L)
		.build();
		Long id = jPService.register(dto);
		log.info("id = {}", id);
	}

	@Test
	public void modifyTest() {
		JoinProjectDTO dto =JoinProjectDTO.builder()
		.id(4L)
		.title("modifyTest2")
		.description("testDescription")
		.userId(1L)
		.build();
		jPService.modify(dto);
	}

	@Test
	public void removeTest() {
		jPService.remove(3L);
	}
}

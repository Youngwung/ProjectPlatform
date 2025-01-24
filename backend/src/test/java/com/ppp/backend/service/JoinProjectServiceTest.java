package com.ppp.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ppp.backend.dto.JoinProjectDTO;
import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class JoinProjectServiceTest {
	@Autowired
	private JoinProjectService jPService;

	// @Test
	public void getTest() {
		Long jPNo = 1L;

		log.info("get = {}", jPService.get(jPNo));
	}

	// @Test
	public void registerTest() {
		JoinProjectDTO dto =JoinProjectDTO.builder()
		.title("registerTest")
		.description("testDescription")
		.userId(1L)
		.build();
		Long id = jPService.register(dto);
		log.info("id = {}", id);
	}

	// @Test
	public void modifyTest() {
		JoinProjectDTO dto =JoinProjectDTO.builder()
		.id(4L)
		.title("modifyTest2")
		.description("testDescription")
		.userId(1L)
		.build();
		jPService.modify(dto);
	}

	// @Test
	public void removeTest() {
		jPService.remove(3L);
	}

	// @Test
	public void insertDummyData() {
		for(int i = 0; i < 20; i++) {
			JoinProjectDTO dto =JoinProjectDTO.builder()
			.title("title: " + i)
			.description("testDescription" + i)
			.userId(1L)
			.build();
			Long id = jPService.register(dto);
			log.info("id = {}", id);
		}
	}

	// @Test
	public void getListTest() {
		// 값을 지정하지 않으면 page = 1, size = 10
		PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).build();
		PageResponseDTO<JoinProjectDTO> result = jPService.getList(pageRequestDTO);
		System.out.println(result);
	}

}

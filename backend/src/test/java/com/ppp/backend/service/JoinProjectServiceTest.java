package com.ppp.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ppp.backend.dto.JoinProjectDTO;
import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
import com.ppp.backend.repository.SkillLevelRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class JoinProjectServiceTest {
	@Autowired
	private JoinProjectService jPService;
	@Autowired
	private SkillLevelRepository skillLevelRepo;

	// @Test
	// @Transactional
	public void getTest() {
		Long jPNo = 36L;
		JoinProjectDTO joinProjectDTO = jPService.get(jPNo);
		System.out.println(joinProjectDTO);
	}

	@Test
	public void registerTest() {
		JoinProjectDTO dto =JoinProjectDTO.builder()
		.title("registerSkillTest")
		.description("testDescription")
		.userId(1L)
		.skills("#React:고급, #Java:중급, #Python:초급")
		.status("진행_중")
		.build();
		Long id = jPService.register(dto);
		log.info("id = {}", id);
	}

	@Test
	public void modifyTest() {
		// SkillLevel newLevelObj = skillLevelRepo.findByNameIgnoreCase("고급");
		// log.info("newLevelObj = {}", newLevelObj);
		String skills = "#React:고급, #Java:초급";
		JoinProjectDTO dto =JoinProjectDTO.builder()
		.id(37L)
		.title("modifySkillTest3")
		.description("testSkillDescription")
		.skills(skills)
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

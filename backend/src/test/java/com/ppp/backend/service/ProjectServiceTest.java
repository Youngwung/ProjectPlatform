package com.ppp.backend.service;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
import com.ppp.backend.dto.ProjectDTO;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class ProjectServiceTest {
	@Autowired
	private ProjectService projectService;

	// @Test
	@Transactional
	public void getTest() {
		Long jPNo = 46L;
		ProjectDTO projectDTO = projectService.get(jPNo);
		System.out.println(projectDTO);
	}

	// @Test
	@Transactional
	public void registerTest() {
		ProjectDTO dto =ProjectDTO.builder()
		.title("registerSkillTest")
		.description("testDescription")
		.userId(3L)
		.skills("#React:고급, #Java:중급")
		.status("진행_중")
		.type("all")
		.maxPeople(6)
		.build();
		Long id = projectService.register(dto);
		log.info("id = {}", id);
	}

	// @Test
	public void modifyTest() {
		String skills = "#React:고급, #Java:초급";
		ProjectDTO dto =ProjectDTO.builder()
		.id(46L)
		.title("modifySkillTest3")
		.description("testSkillDescription")
		.skills(skills)
		.type("all")
		.userId(1L)
		.isPublic(false)
		.maxPeople(4)
		.status("모집_중")
		.build();
		projectService.modify(dto);
	}

	// @Test
	public void removeTest() {
		projectService.remove(38L);
	}

	// @Test
	public void insertDummyData() {
		for(int i = 0; i < 20; i++) {
			ProjectDTO dto =ProjectDTO.builder()
			.title("title: " + i)
			.description("testDescription" + i)
			.userId(1L)
			.build();
			Long id = projectService.register(dto);
			log.info("id = {}", id);
		}
	}

	// @Test
	public void getListTest() {
		// 값을 지정하지 않으면 page = 1, size = 10
		PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).build();
		PageResponseDTO<ProjectDTO> result = projectService.getList(pageRequestDTO);
		System.out.println(result);
	}

	@Test
	public void getSearchResultTest() {
		List<String> skills = Arrays.asList("java", "react", "python");
		PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
			.query("e")
			.querySkills(skills)
		.build();
		PageResponseDTO<ProjectDTO> searchResult = projectService.getSearchResult(pageRequestDTO);
		log.info("searchResult-----------{}", searchResult);
	}

}

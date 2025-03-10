package com.ppp.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.ppp.backend.dto.skill.SkillSearchDto;
import com.ppp.backend.dto.skill.SkillSearchResultDto;

import lombok.extern.slf4j.Slf4j;

// @SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class SkillSearchServiceTest {
	@Autowired
	private SkillSearchService skillSearchService;

	// @Test
	public void getListTest() {
		List<SkillSearchDto> list = skillSearchService.getList();
		System.out.println(list);
	}

	// @Test
	public void SearchSkillTest() {
		List<SkillSearchResultDto> searchResult = skillSearchService.getSearchResult("av");
		System.out.println(searchResult);
	}
}

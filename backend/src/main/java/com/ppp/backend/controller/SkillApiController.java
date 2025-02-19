package com.ppp.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ppp.backend.dto.skill.SkillSearchDto;
import com.ppp.backend.dto.skill.SkillSearchResultDto;
import com.ppp.backend.service.SkillSearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/skill")
public class SkillApiController {
	private final SkillSearchService skillSearchService;

	@GetMapping("/category/list")
	public List<SkillSearchDto> getList() {
		List<SkillSearchDto> dtoList = skillSearchService.getList();
		return dtoList;
	}

	@GetMapping("/search")
	public List<SkillSearchResultDto> getSearchResult(@RequestParam(name = "skillQuery") String skillQuery) {
		log.info("skillQuery = {}", skillQuery);
		List<SkillSearchResultDto> searchResult = skillSearchService.getSearchResult(skillQuery);
		return searchResult;
	}
	
	
}

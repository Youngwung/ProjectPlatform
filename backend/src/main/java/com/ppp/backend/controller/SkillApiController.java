package com.ppp.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ppp.backend.dto.UserDto;
import com.ppp.backend.dto.skill.SkillSearchDto;
import com.ppp.backend.dto.skill.SkillSearchResultDto;
import com.ppp.backend.service.SkillSearchService;
import com.ppp.backend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/skill")
public class SkillApiController {
	private final SkillSearchService skillSearchService;
	private final UserService userService;
	private final AuthApiController authApi;

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

	@GetMapping("/user")
	public String getUserSkill(HttpServletRequest request) {
		Long userId = authApi.extractUserIdFromCookie(request);

		String skill = userService.getSkill(userId);
		return skill;
	}

	@PutMapping("/user")
	public Map<String, String> modifyUserSkill(HttpServletRequest request, @RequestParam(name = "skills") String	skills) {
		log.info("StringSkill = {}", skills);
		Long userId = authApi.extractUserIdFromCookie(request);
		// 스킬 업데이터 메서드 호출
		UserDto userDto = UserDto.builder().skills(skills).build();
		userService.updateUserInfo(userId, userDto);
		return Map.of("RESULT", "SUCCESS");
	}

}

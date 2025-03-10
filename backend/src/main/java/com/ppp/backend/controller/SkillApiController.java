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
import com.ppp.backend.util.AuthUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * SkillApiController는 스킬 관련 API를 제공함.
 *
 * 주요 기능:
 * - 스킬 카테고리 목록 조회
 * - 스킬 검색 결과 조회
 * - 사용자 스킬 조회
 * - 사용자 스킬 수정
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/skill")
public class SkillApiController {

	private final SkillSearchService skillSearchService;
	private final UserService userService;
	private final AuthUtil authUtil;

	/**
	 * 스킬 카테고리 목록 조회 API.
	 *
	 * @return 스킬 카테고리 목록(List&lt;SkillSearchDto&gt;) 반환함
	 */
	@GetMapping("/category/list")
	public List<SkillSearchDto> getList() {
		List<SkillSearchDto> dtoList = skillSearchService.getList();
		return dtoList;
	}

	/**
	 * 스킬 검색 결과 조회 API.
	 *
	 * @param skillQuery 검색할 스킬 문자열 전달함
	 * @return 검색 결과 목록(List&lt;SkillSearchResultDto&gt;) 반환함
	 */
	@GetMapping("/search")
	public List<SkillSearchResultDto> getSearchResult(@RequestParam(name = "skillQuery") String skillQuery) {
		log.info("skillQuery = {}", skillQuery);
		List<SkillSearchResultDto> searchResult = skillSearchService.getSearchResult(skillQuery);
		return searchResult;
	}

	/**
	 * 사용자 스킬 조회 API.
	 *
	 * @param request HTTP 요청 객체 전달함 (쿠키에서 userId 추출)
	 * @return 조회된 사용자 스킬(String) 반환함
	 */
	@GetMapping("/user")
	public String getUserSkill(HttpServletRequest request) {
		Long userId = authUtil.extractUserIdFromCookie(request);
		String skill = userService.getSkill(userId);
		return skill;
	}

	/**
	 * 사용자 스킬 수정 API.
	 *
	 * @param request HTTP 요청 객체 전달함 (쿠키에서 userId 추출)
	 * @param skills  수정할 스킬 문자열 전달함 (RequestParam)
	 * @return 수정 결과를 나타내는 Map (성공 시 "RESULT": "SUCCESS") 반환함
	 */
	@PutMapping("/user")
	public Map<String, String> modifyUserSkill(HttpServletRequest request, @RequestParam(name = "skills") String skills) {
		log.info("StringSkill = {}", skills);
		Long userId = authUtil.extractUserIdFromCookie(request);
		// 스킬 업데이트를 위한 UserDto 생성 후 수정함
		UserDto userDto = UserDto.builder().skills(skills).build();
		userService.updateUserInfo(userId, userDto);
		return Map.of("RESULT", "SUCCESS");
	}
}

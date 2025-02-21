package com.ppp.backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
import com.ppp.backend.dto.ProjectDTO;
import com.ppp.backend.service.ProjectService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/project")
public class ProjectController {

	private final ProjectService projectService;
	private final AuthApiController authApiController;

	@GetMapping("/{projectId}")
	public ProjectDTO get(@PathVariable("projectId") Long projectId) {
		ProjectDTO projectDTO = projectService.get(projectId);
		log.info("dto = {}", projectDTO);
		return projectDTO;
	}

	@GetMapping("/list")
	public PageResponseDTO<ProjectDTO> getList(PageRequestDTO pageRequestDTO) {
		log.info("list = {}", pageRequestDTO);
		
		return projectService.getList(pageRequestDTO);
	}

	@GetMapping("/search")
	public PageResponseDTO<ProjectDTO> getMethodName(PageRequestDTO pageRequestDTO) {
		log.info("Request = {}", pageRequestDTO);
		return projectService.getSearchResult(pageRequestDTO);
	}
	

	@PostMapping("/")
	public Map<String, Long> register(HttpServletRequest request, @RequestBody ProjectDTO dto) {
		/*
		 * 포스트맨 등록 기능 JSON데이터 
{
    "title": "restControllerTest",
    "description": "restControllerTest description",
    "userId": "1"
}
		 */
		Long userId = authApiController.extractUserIdFromCookie(request);

		if (userId == null) {
			log.error("❌ 쿠키에서 userId를 가져오지 못했습니다.");
			return Map.of("projectId", -1L);
		}
		dto.setUserId(userId);
		log.info("dto = {}", dto);

		Long projectId = projectService.register(dto);
		
		// JSON 형태로 값을 전달하기 위해 Map으로 리턴
		return Map.of("projectId", projectId);
	}

	@PutMapping("/{projectId}")
	public Map<String, String> modify(HttpServletRequest request, @PathVariable("projectId") Long projectId, @RequestBody ProjectDTO dto) {
		/*
		 * 포스트맨 수정 기능 JSON데이터 
{
    "title": "restControllerModifyTest",
    "description": "restControllerModifyTest description",
    "userId": "1"
}
		 */
		Long userId = authApiController.extractUserIdFromCookie(request);

		// 경로변수와 번호를 동기화
		if (userId == null) {
			log.error("❌ 쿠키에서 userId를 가져오지 못했습니다.");
			return Map.of("projectId", "FAIL");
		}
		dto.setUserId(userId);
		log.info("dto = {}", dto);
		dto.setId(projectId);

		projectService.modify(dto);
		
		return Map.of("RESULT", "SUCCESS");
	}

	@DeleteMapping("/{projectId}")
	public Map<String, String> remove(HttpServletRequest request, @PathVariable("projectId") Long projectId) {
		Long userId = authApiController.extractUserIdFromCookie(request);

		// 경로변수와 번호를 동기화
		if (userId == null) {
			log.error("❌ 쿠키에서 userId를 가져오지 못했습니다.");
			return Map.of("projectId", "FAIL");
		}
		projectService.remove(projectId);
		return Map.of("RESULT", "SUCCESS");
	}
}

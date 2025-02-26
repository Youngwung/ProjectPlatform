package com.ppp.backend.controller;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
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
import com.ppp.backend.util.AuthUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ProjectController는 프로젝트 관련 API를 제공함.
 *
 * 주요 기능:
 * - 프로젝트 단건 조회
 * - 프로젝트 목록 조회 (페이징 처리 포함)
 * - 프로젝트 검색 결과 조회
 * - 프로젝트 등록
 * - 프로젝트 수정
 * - 프로젝트 삭제
 * - 내 프로젝트 조회
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/project")
public class ProjectController {

	private final ProjectService projectService;
	private final AuthUtil authUtil;

	/**
	 * 프로젝트 단건 조회 API.
	 *
	 * @param projectId 조회할 프로젝트의 고유 ID 전달함
	 * @return 조회된 ProjectDTO 반환함
	 */
	@GetMapping("/{projectId}")
	public ProjectDTO get(@PathVariable("projectId") Long projectId) {
		ProjectDTO projectDTO = projectService.get(projectId);
		log.info("get() dto = {}", projectDTO);
		return projectDTO;
	}

	/**
	 * 프로젝트 목록 조회 API (페이징 처리 포함).
	 *
	 * @param pageRequestDTO 페이징 및 정렬 정보를 담은 PageRequestDTO 전달함
	 * @return 조회된 프로젝트 목록을 포함하는 PageResponseDTO&lt;ProjectDTO&gt; 반환함
	 */
	@GetMapping("/list")
	public PageResponseDTO<ProjectDTO> getList(PageRequestDTO pageRequestDTO) {
		log.info("list 요청: {}", pageRequestDTO);
		return projectService.getList(pageRequestDTO);
	}

	/**
	 * 프로젝트 검색 결과 조회 API.
	 *
	 * @param pageRequestDTO 검색 조건 및 페이징 정보를 담은 PageRequestDTO 전달함
	 * @return 검색 결과를 포함하는 PageResponseDTO&lt;ProjectDTO&gt; 반환함
	 */
	@GetMapping("/search")
	public PageResponseDTO<ProjectDTO> getMethodName(PageRequestDTO pageRequestDTO) {
		log.info("Request = {}", pageRequestDTO);
		return projectService.getSearchResult(pageRequestDTO);
	}

	/**
	 * 프로젝트 등록 API.
	 *
	 * @param request HTTP 요청 객체 전달함 (쿠키에서 userId 추출)
	 * @param dto     등록할 프로젝트 정보를 담은 ProjectDTO 전달함
	 * @return "projectId" 키에 등록된 프로젝트 ID가 담긴 Map 반환함
	 */
	@PostMapping("/")
	public Map<String, Long> register(HttpServletRequest request, @RequestBody ProjectDTO dto) {
		// 포스트맨 등록 기능 예시 JSON:
		// {
		//    "title": "restControllerTest",
		//    "description": "restControllerTest description",
		//    "userId": "1"
		// }
		Long userId = authUtil.extractUserIdFromCookie(request);
		if (userId == null) {
			log.error("❌ 쿠키에서 userId를 가져오지 못했음");
			return Map.of("projectId", -1L);
		}
		dto.setUserId(userId);
		log.info("register() dto = {}", dto);
		Long projectId = projectService.register(dto);
		return Map.of("projectId", projectId);
	}

	/**
	 * 프로젝트 수정 API.
	 *
	 * @param request   HTTP 요청 객체 전달함 (쿠키에서 userId 추출)
	 * @param projectId 수정할 프로젝트의 고유 ID 전달함 (PathVariable)
	 * @param dto       수정할 프로젝트 정보를 담은 ProjectDTO 전달함
	 * @return 수정 결과를 나타내는 Map (성공 시 "RESULT": "SUCCESS") 반환함
	 */
	@PutMapping("/{projectId}")
	public Map<String, String> modify(HttpServletRequest request, @PathVariable("projectId") Long projectId, @RequestBody ProjectDTO dto) {
		// 포스트맨 수정 기능 예시 JSON:
		// {
		//    "title": "restControllerModifyTest",
		//    "description": "restControllerModifyTest description",
		//    "userId": "1"
		// }
		Long userId = authUtil.extractUserIdFromCookie(request);
		if (userId == null) {
			log.error("❌ 쿠키에서 userId를 가져오지 못했음");
			return Map.of("projectId", "FAIL");
		}
		dto.setUserId(userId);
		log.info("modify() dto = {}", dto);
		dto.setId(projectId);
		projectService.modify(dto);
		return Map.of("RESULT", "SUCCESS");
	}

	/**
	 * 프로젝트 삭제 API.
	 *
	 * @param request   HTTP 요청 객체 전달함 (쿠키에서 userId 추출)
	 * @param projectId 삭제할 프로젝트의 고유 ID 전달함 (PathVariable)
	 * @return 삭제 결과를 나타내는 Map (성공 시 "RESULT": "SUCCESS") 반환함
	 */
	@DeleteMapping("/{projectId}")
	public Map<String, String> remove(HttpServletRequest request, @PathVariable("projectId") Long projectId) {
		Long userId = authUtil.extractUserIdFromCookie(request);
		if (userId == null) {
			log.error("❌ 쿠키에서 userId를 가져오지 못했음");
			return Map.of("projectId", "FAIL");
		}
		projectService.remove(projectId);
		return Map.of("RESULT", "SUCCESS");
	}

	/**
	 * 내 프로젝트 조회 API.
	 *
	 * @param request HTTP 요청 객체 전달함 (쿠키에서 userId 추출)
	 * @return 조회된 내 프로젝트 목록(List&lt;ProjectDTO&gt;) 반환함
	 */
	@GetMapping("/my")
	public List<ProjectDTO> getMyProjects(HttpServletRequest request) {
		log.info("✅ [GET] /api/project/my - 내 프로젝트 조회 요청");
		return projectService.getMyProjects(request);
	}
}

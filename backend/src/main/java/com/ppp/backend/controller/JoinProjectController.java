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

import com.ppp.backend.dto.JoinProjectDTO;
import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
import com.ppp.backend.service.JoinProjectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/joinProject")
public class JoinProjectController {

	private final JoinProjectService joinProjectService;

	@GetMapping("/{jpNo}")
	public JoinProjectDTO get(@PathVariable("jpNo") Long jpNo) {
		// 테스트 완료
		// ? 로그에 한글이 깨지는 문제가 있음.
		JoinProjectDTO joinProjectDTO = joinProjectService.get(jpNo);
		log.info("dto = {}", joinProjectDTO);
		return joinProjectDTO;
	}

	@GetMapping("/list")
	public PageResponseDTO<JoinProjectDTO> getList(PageRequestDTO pageRequestDTO) {
		log.info("list = {}", pageRequestDTO);
		
		return joinProjectService.getList(pageRequestDTO);
	}

	@PostMapping("/")
	public Map<String, Long> register(@RequestBody JoinProjectDTO dto) {
		/*
		 * 포스트맨 등록 기능 JSON데이터 
{
    "title": "restControllerTest",
    "description": "restControllerTest description",
    "userId": "1"
}
		 */
		log.info("dto = {}", dto);

		Long jpNo = joinProjectService.register(dto);
		
		// JSON 형태로 값을 전달하기 위해 Map으로 리턴
		return Map.of("JPNO", jpNo);
	}

	@PutMapping("/{jpNo}")
	public Map<String, String> modify(@PathVariable("jpNo") Long jpNo, @RequestBody JoinProjectDTO dto) {
		/*
		 * 포스트맨 수정 기능 JSON데이터 
{
    "title": "restControllerModifyTest",
    "description": "restControllerModifyTest description",
    "userId": "1"
}
		 */
		// 경로변수와 번호를 동기화
		dto.setId(jpNo);

		joinProjectService.modify(dto);
		
		return Map.of("RESULT", "SUCCESS");
	}

	@DeleteMapping("/{jpNo}")
	public Map<String, String> remove(@PathVariable("jpNo") Long jpNo) {
		joinProjectService.remove(jpNo);
		return Map.of("RESULT", "SUCCESS");
	}
}

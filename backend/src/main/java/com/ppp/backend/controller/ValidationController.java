package com.ppp.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ppp.backend.dto.ProjectDTO;
import com.ppp.backend.service.ValidationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/validation")
@RequiredArgsConstructor
public class ValidationController {

	 private final ValidationService validService;
	
	@PostMapping("/project")
	public ResponseEntity<Map<?, ?>> getMethodName(@RequestBody ProjectDTO dto) {
		// 클라이언트에서 전달받은 스킬이 DB에 있는 지 검사, DB에 없는 리스트 리턴
		List<String> wrongList = validService.validateSkills(dto.getSkills());
		// 검사 결과 저장을 위한 필드 선언
		boolean isValid;

		if (!wrongList.isEmpty()) {
			// 검사 결과 DB에 없는 단어가 사용된 경우
			isValid = false;
		} 
		else {
			// 검사 결과 DB에 있는 단어만 사용된 경우
			isValid = true;
		}
		Map<?, ?> response = Map.of("isValid", isValid, "wrongString", wrongList);
		log.info("response = {}", response);
		return ResponseEntity.ok(Map.of("isValid", isValid, "wrongString", wrongList));
	}
}
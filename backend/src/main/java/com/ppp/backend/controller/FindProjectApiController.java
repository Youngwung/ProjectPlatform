package com.ppp.backend.controller;

import com.ppp.backend.dto.FindProjectDto;
import com.ppp.backend.domain.FindProject;
import com.ppp.backend.service.FindProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/findProject")
@RequiredArgsConstructor
@Slf4j
public class FindProjectApiController {

    private final FindProjectService findProjectService;

    // **1. 전체 프로젝트 조회 (GET)**
    @GetMapping("/list")
    public ResponseEntity<List<FindProjectDto>> getAllFindProjects() {
        List<FindProjectDto> projectList = findProjectService.getAllFindProjects();
        log.info("프로젝트 전체조회 요청: {}", projectList);
        return ResponseEntity.ok(projectList);
    }
    // **2. 검색어 기반 프로젝트 조회 (GET)**
    @GetMapping("/search")
    public List<FindProjectDto> searchFindProjects(@RequestParam String searchTerm) {
        log.info("프로젝트 검색 요청: {}", searchTerm);
        return findProjectService.searchFindProjects(searchTerm);
    }

    // **3. 프로젝트 상세 조회 (GET)**
    @GetMapping("/list/{id}")
    public FindProjectDto getFindProjectById(@PathVariable Long id) {
        log.info("프로젝트 상세 조회 요청: {}", id);
        return findProjectService.getFindProjectById(id);
    }

    // **4. 새 프로젝트 생성 (POST)**
    @PostMapping
    public ResponseEntity<FindProjectDto> createFindProject(@RequestBody FindProjectDto findProjectDto) {
        log.info("새로운 프로젝트 생성 요청: {}", findProjectDto.getTitle());
        FindProjectDto createdProject = findProjectService.createFindProject(findProjectDto);
        return ResponseEntity.ok(createdProject);
    }

    // **5. 기존 프로젝트 수정 (PUT)**
    @PutMapping("/{id}")
    public ResponseEntity<FindProjectDto> updateFindProject(
            @PathVariable Long id,
            @RequestBody FindProjectDto findProjectDto
    ) {
        log.info("프로젝트 수정 요청: ID={}, Data={}", id, findProjectDto);
        FindProjectDto updatedProject = findProjectService.updateFindProject(id, findProjectDto);
        return ResponseEntity.ok(updatedProject);
    }

    // **6. 프로젝트 삭제 (DELETE)**
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFindProject(@PathVariable Long id) {
        log.info("프로젝트 삭제 요청: ID={}", id);
        findProjectService.deleteFindProject(id);
        return ResponseEntity.ok("프로젝트가 성공적으로 삭제되었습니다.");
    }
}

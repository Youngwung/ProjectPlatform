package com.ppp.backend.controller;

import com.ppp.backend.dto.PortpolioDto;
import com.ppp.backend.service.PortpolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Portpolio")
@RequiredArgsConstructor
@Slf4j
public class PortpolioApiController {

    private final PortpolioService PortpolioService;

    // **1. 전체 프로젝트 조회 (GET)**
    @GetMapping("/list")
    public ResponseEntity<List<PortpolioDto>> getAllPortpolios() {
        List<PortpolioDto> projectList = PortpolioService.getAllPortpolios();
        log.info("프로젝트 전체조회 요청: {}", projectList);
        return ResponseEntity.ok(projectList);
    }
    // **2. 검색어 기반 프로젝트 조회 (GET)**
    @GetMapping("/search")
    public List<PortpolioDto> searchPortpolios(@RequestParam String searchTerm) {
        log.info("프로젝트 검색 요청: {}", searchTerm);
        return PortpolioService.searchPortpolios(searchTerm);
    }

    // **3. 프로젝트 상세 조회 (GET)**
    @GetMapping("/list/{id}")
    public PortpolioDto getPortpolioById(@PathVariable Long id) {
        log.info("프로젝트 상세 조회 요청: {}", id);
        return PortpolioService.getPortpolioById(id);
    }

    // **4. 새 프로젝트 생성 (POST)**
    @PostMapping
    public ResponseEntity<PortpolioDto> createPortpolio(@RequestBody PortpolioDto PortpolioDto) {
        log.info("새로운 프로젝트 생성 요청: {}", PortpolioDto.getTitle());
        PortpolioDto createdProject = PortpolioService.createPortpolio(PortpolioDto);
        return ResponseEntity.ok(createdProject);
    }

    // **5. 기존 프로젝트 수정 (PUT)**
    @PutMapping("/{id}")
    public ResponseEntity<PortpolioDto> updatePortpolio(
            @PathVariable Long id,
            @RequestBody PortpolioDto PortpolioDto
    ) {
        log.info("프로젝트 수정 요청: ID={}, Data={}", id, PortpolioDto);
        PortpolioDto updatedProject = PortpolioService.updatePortpolio(id, PortpolioDto);
        return ResponseEntity.ok(updatedProject);
    }

    // **6. 프로젝트 삭제 (DELETE)**
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePortpolio(@PathVariable Long id) {
        log.info("프로젝트 삭제 요청: ID={}", id);
        PortpolioService.deletePortpolio(id);
        return ResponseEntity.ok("프로젝트가 성공적으로 삭제되었습니다.");
    }
}

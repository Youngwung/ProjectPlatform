package com.ppp.backend.controller;

import com.ppp.backend.dto.PortfolioDto;
import com.ppp.backend.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
@Slf4j
public class PortfolioApiController {

    private final PortfolioService PortfolioService;

    // **1. 전체 프로젝트 조회 (GET)**
    @GetMapping("/list")
    public ResponseEntity<List<PortfolioDto>> getAllPortfolios() {
        List<PortfolioDto> projectList = PortfolioService.getAllPortfolios();
        log.info("프로젝트 전체조회 요청: {}", projectList);
        return ResponseEntity.ok(projectList);
    }
    // **2. 검색어 기반 프로젝트 조회 (GET)**
    @GetMapping("/search")
    public List<PortfolioDto> searchPortfolios(@RequestParam String searchTerm) {
        log.info("프로젝트 검색 요청: {}", searchTerm);
        return PortfolioService.searchPortfolios(searchTerm);
    }

    // **3. 프로젝트 상세 조회 (GET)**
    @GetMapping("/list/{id}")
    public PortfolioDto getPortfolioById(@PathVariable Long id) {
        log.info("프로젝트 상세 조회 요청: {}", id);
        return PortfolioService.getPortfolioById(id);
    }

    // **4. 새 프로젝트 생성 (POST)**
    @PostMapping
    public ResponseEntity<PortfolioDto> createPortfolio(@RequestBody PortfolioDto PortfolioDto) {
        log.info("새로운 프로젝트 생성 요청: {}", PortfolioDto.getTitle());
        PortfolioDto createdProject = PortfolioService.createPortfolio(PortfolioDto);
        return ResponseEntity.ok(createdProject);
    }

    // **5. 기존 프로젝트 수정 (PUT)**
    @PutMapping("/{id}")
    public ResponseEntity<PortfolioDto> updatePortfolio(
            @PathVariable Long id,
            @RequestBody PortfolioDto PortfolioDto
    ) {
        log.info("프로젝트 수정 요청: ID={}, Data={}", id, PortfolioDto);
        PortfolioDto updatedProject = PortfolioService.updatePortfolio(id, PortfolioDto);
        return ResponseEntity.ok(updatedProject);
    }

    // **6. 프로젝트 삭제 (DELETE)**
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePortfolio(@PathVariable Long id) {
        log.info("프로젝트 삭제 요청: ID={}", id);
        PortfolioService.deletePortfolio(id);
        return ResponseEntity.ok("프로젝트가 성공적으로 삭제되었습니다.");
    }
}

package com.ppp.backend.controller;

import com.ppp.backend.dto.PortfolioDto;
import com.ppp.backend.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
@Slf4j
public class PortfolioApiController {

    private final PortfolioService portfolioService;


    // **1. 전체 프로젝트 조회 (GET)**
    @GetMapping("/list")
    public ResponseEntity<List<PortfolioDto>> getAllPortfolios() {
        List<PortfolioDto> projectList = portfolioService.getAllPortfolios();
        log.info("프로젝트 전체조회 요청: {}", projectList);
        return ResponseEntity.ok(projectList);
    }
    // **2. 검색어 기반 프로젝트 조회 (GET)**
    @GetMapping("/search")
    public List<PortfolioDto> searchPortfolios(@RequestParam String searchTerm) {
        log.info("프로젝트 검색 요청: {}", searchTerm);
        return portfolioService.searchPortfolios(searchTerm);
    }

    // **3. 프로젝트 상세 조회 (GET)**
    @GetMapping("/list/{id}")
    public PortfolioDto getPortfolioById(@PathVariable Long id) {
        log.info("프로젝트 상세 조회 요청: {}", id);
        return portfolioService.getPortfolioById(id);
    }

    // **4. 새 프로젝트 생성 (POST)**
    @PostMapping("/create")
    public ResponseEntity<PortfolioDto> createPortfolio(
            @RequestBody PortfolioDto portfolioDto,
            @AuthenticationPrincipal UserDetails userDetails // ✅ JWT에서 유저 정보 가져오기
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // ✅ JWT에서 `userId` 추출하여 자동 설정
        Long userId = Long.parseLong(userDetails.getUsername());
        portfolioDto.setUserId(userId);
        PortfolioDto createdProject = portfolioService.createPortfolio(portfolioDto);
        return ResponseEntity.ok(createdProject);
    }

    // **5. 기존 프로젝트 수정 (PUT)**
    @PutMapping("/{id}")
    public ResponseEntity<PortfolioDto> updatePortfolio(
            @PathVariable Long id,
            @RequestBody PortfolioDto PortfolioDto
    ) {
        log.info("프로젝트 수정 요청: ID={}, Data={}", id, PortfolioDto);
        PortfolioDto updatedProject = portfolioService.updatePortfolio(id, PortfolioDto);
        return ResponseEntity.ok(updatedProject);
    }

    // **6. 프로젝트 삭제 (DELETE)**
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePortfolio(@PathVariable Long id) {
        log.info("프로젝트 삭제 요청: ID={}", id);
        portfolioService.deletePortfolio(id);
        return ResponseEntity.ok("프로젝트가 성공적으로 삭제되었습니다.");
    }
}

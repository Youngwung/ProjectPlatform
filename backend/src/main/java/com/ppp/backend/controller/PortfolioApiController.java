package com.ppp.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.ppp.backend.dto.PortfolioDto;
import com.ppp.backend.security.CustomUserDetails;
import com.ppp.backend.service.PortfolioService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
@Slf4j
public class PortfolioApiController {

    private final PortfolioService portfolioService;


    // **1. 전체 프로젝트 조회 (GET)**
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<PortfolioDto>> getAllPortfolios() {
        PageResponseDTO<PortfolioDto> projectList = portfolioService.getAllPortfolios(PageRequestDTO.builder().build());
        log.info("프로젝트 전체조회 요청: {}", projectList);
        return ResponseEntity.ok(projectList);
    }
    
    // **3. 프로젝트 상세 조회 (GET)**
    @GetMapping("/list/{id}")
    public PortfolioDto getPortfolioById(@PathVariable(name = "id") Long id) {
        log.info("프로젝트 상세 조회 요청: {}", id);
        return portfolioService.getPortfolioById(id);
    }

    // **4. 새 프로젝트 생성 (POST)**
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PortfolioDto> createPortfolio(
            @RequestBody PortfolioDto portfolioDto,
            @AuthenticationPrincipal CustomUserDetails userDetails // ✅ JWT에서 유저 정보 가져오기
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Long userId = userDetails.getUserId();
        // ✅ JWT에서 `userId` 추출하여 자동 설정
        log.info("userDetails: {}", userDetails);
        String userName = userDetails.getUsername();
        log.info("userName유저 이름: {}", userName);

        PortfolioDto createdProject = portfolioService.createPortfolio(portfolioDto,userId);
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

    @GetMapping("/search")
    public PageResponseDTO<PortfolioDto> getSearchResult(PageRequestDTO pageRequestDTO) {
        log.info("Request = {}", pageRequestDTO);
        return portfolioService.getSearchResult(pageRequestDTO);
    }
    
}

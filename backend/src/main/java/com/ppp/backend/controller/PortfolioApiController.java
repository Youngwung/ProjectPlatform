package com.ppp.backend.controller;

import java.util.List;

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
import com.ppp.backend.util.AuthUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
@Slf4j
public class PortfolioApiController {

    private final PortfolioService portfolioService;
    private final AuthUtil authUtil;

    // **1. 전체 포폴 조회 (GET)**
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<PortfolioDto>> getAllPortfolios() {
        PageResponseDTO<PortfolioDto> projectList = portfolioService.getAllPortfolios(PageRequestDTO.builder().build());
        log.info("프로젝트 전체조회 요청: {}", projectList);
        return ResponseEntity.ok(projectList);
    }
    // **2. 특정 사용자 포폴 조회(GET)**
    @GetMapping("/my")
    public ResponseEntity<List<PortfolioDto>> getMyPortfolios(HttpServletRequest request) {
        Long userId = authUtil.extractUserIdFromCookie(request);
        if (userId == null) {
            return ResponseEntity.status(401).build(); // ❌ 인증 실패 (401 Unauthorized)
        }

        log.info("🔎 [Portfolio 조회] userId={}", userId);
        List<PortfolioDto> myPortfolios = portfolioService.getMyPortfolios(userId);

        return ResponseEntity.ok(myPortfolios); // ✅ 포트폴리오 목록 반환
    }

    // **3. 포폴 상세 조회 (GET)**
    @GetMapping("/list/{id}")
    public PortfolioDto getPortfolioById(@PathVariable(name = "id") Long id) {
        log.info("포폴 상세 조회 요청: {}", id);
        return portfolioService.getPortfolioById(id);
    }

    // **4. 새 포폴 생성 (POST)**
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

    // **5. 기존 포폴 수정 (PUT)**
    @PutMapping("/{id}")
    public ResponseEntity<PortfolioDto> updatePortfolio(
            @PathVariable(name = "id") Long id,
            @RequestBody PortfolioDto PortfolioDto
    ) {
        log.info("포폴 수정 요청: ID={}, Data={}", id, PortfolioDto);
        PortfolioDto updatedProject = portfolioService.updatePortfolio(id, PortfolioDto);
        return ResponseEntity.ok(updatedProject);
    }

    // **6. 포폴 삭제 (DELETE)**
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePortfolio(@PathVariable(name = "id") Long id) {
        log.info("포폴 삭제 요청: ID={}", id);
        portfolioService.deletePortfolio(id);
        return ResponseEntity.ok("프로젝트가 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/search")
    public PageResponseDTO<PortfolioDto> getSearchResult(PageRequestDTO pageRequestDTO) {
        log.info("Request = {}", pageRequestDTO);
        return portfolioService.getSearchResult(pageRequestDTO);
    }
    
}

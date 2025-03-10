package com.ppp.backend.controller;

import com.ppp.backend.dto.LinkDto;
import com.ppp.backend.service.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/link")
@RequiredArgsConstructor
@Slf4j
public class LinkApiController {

    private final LinkService linkService;

    /* ========================= 🔹 조회 관련 API 🔹 ========================= */

    /** ✅ 전체 링크 조회 */
    @GetMapping("/list")
    public ResponseEntity<List<LinkDto>> getAllLinks() {
        log.info("🔍 [전체 링크 조회 요청] 시작");
        List<LinkDto> links = linkService.getAllLinks();
        log.info("🔍 [전체 링크 조회 성공] 조회된 링크 수: {}", links.size());
        return ResponseEntity.ok(links);
    }

    /** ✅ 특정 링크 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<LinkDto> getLinkById(@PathVariable Long id) {
        log.info("🔍 [링크 상세 조회 요청] ID={}", id);
        LinkDto link = linkService.getLinkById(id);
        log.info("🔍 [링크 상세 조회 성공] ID={} / Link: {}", id, link);
        return ResponseEntity.ok(link);
    }

    /* ========================= 🔹 생성 관련 API 🔹 ========================= */

    /** ✅ 새 링크 생성 */
    @PostMapping("/create")
    public ResponseEntity<?> createLink(@RequestBody LinkDto linkDto) {
        log.info("🆕 [새 링크 생성 요청] Payload: {}", linkDto);
        if (linkDto.getUserId() == null) {
            log.warn("⚠ [새 링크 생성 실패] 유저 ID가 누락됨: {}", linkDto);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("유저 ID가 필요합니다.");
        }

        try {
            LinkDto createdLink = linkService.createLink(linkDto.getUserId(), linkDto);
            log.info("✅ [새 링크 생성 성공] 생성된 링크: {}", createdLink);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLink);
        } catch (Exception e) {
            log.error("🚨 [새 링크 생성 실패] 에러: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("링크 생성 중 오류가 발생했습니다.");
        }
    }

    /* ========================= 🔹 수정 관련 API 🔹 ========================= */

    /** ✅ 특정 링크 수정 (유저 검증 추가) */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLink(
            @PathVariable("id") Long id,
            @RequestBody LinkDto linkDto
    ) {
        log.info("✏️ [링크 수정 요청] ID={}, Payload: {}", id, linkDto);
        if (linkDto.getUserId() == null) {
            log.warn("⚠ [링크 수정 실패] 유저 ID가 누락됨: {}", linkDto);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("유저 ID가 필요합니다.");
        }

        try {
            LinkDto updatedLink = linkService.updateUserLink(linkDto.getUserId(), id, linkDto);
            log.info("✅ [링크 수정 성공] 수정된 링크: {}", updatedLink);
            return ResponseEntity.ok(updatedLink);
        } catch (Exception e) {
            log.error("🚨 [링크 수정 실패] ID={} 에러: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("링크 수정 중 오류가 발생했습니다.");
        }
    }

    /* ========================= 🔹 삭제 관련 API 🔹 ========================= */

    /** ✅ 특정 링크 삭제 (유저 검증 추가) */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLink(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        log.info("🗑 [링크 삭제 요청] ID={}, userId={}", id, userId);
        if (userId == null) {
            log.warn("⚠ [링크 삭제 실패] 유저 ID가 누락됨: ID={}, userId={}", id, userId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("유저 ID가 필요합니다.");
        }

        try {
            linkService.deleteUserLink(userId, id);
            log.info("✅ [링크 삭제 성공] 삭제된 링크 ID={}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("🚨 [링크 삭제 실패] ID={} 에러: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("링크 삭제 중 오류가 발생했습니다.");
        }
    }
}

package com.ppp.backend.controller;

import com.ppp.backend.domain.Link;
import com.ppp.backend.dto.LinkDto;
import com.ppp.backend.service.LinkService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/link")
@RequiredArgsConstructor
@Slf4j
public class LinkApiController {

    private final LinkService linkService;

    // **1. 전체 링크 조회 (GET)**
    @GetMapping("/list")
    public ResponseEntity<List<LinkDto>> getAllLinks() {
        log.info("전체 링크 조회 요청");
        List<LinkDto> links = linkService.getAllLinks();
        return ResponseEntity.ok(links);
    }

    // **2. 특정 링크 조회 (GET)**
    @GetMapping("/list/{id}")
    public ResponseEntity<LinkDto> getLinkById(@PathVariable Long id) {
        log.info("링크 상세 조회 요청: {}", id);
        LinkDto link = linkService.getLinkById(id);
        return ResponseEntity.ok(link);
    }

    // **3. 새 링크 생성 (POST)**
    @PostMapping("/create")
    public ResponseEntity<LinkDto> createLink(@RequestBody LinkDto linkDto) {
        log.info("새 링크 생성 요청: {}", linkDto);
        LinkDto createdLink = linkService.createLink(linkDto);
        return ResponseEntity.ok(createdLink);
    }

    // **4. 링크 수정 (PUT)**
    @PutMapping("/{id}")
    public ResponseEntity<LinkDto> updateLink(
            @PathVariable Long id,
            @RequestBody LinkDto linkDto
    ) {
        log.info("링크 수정 요청: ID={}, Data={}", id, linkDto);
        LinkDto updatedLink = linkService.updateLink(id, linkDto);
        return ResponseEntity.ok(updatedLink);
    }

    // **5. 링크 삭제 (DELETE)**
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLink(@PathVariable Long id) {
        log.info("링크 삭제 요청: {}", id);
        linkService.deleteLink(id);
        return ResponseEntity.noContent().build();
    }
}

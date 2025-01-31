package com.ppp.backend.controller;

import com.ppp.backend.dto.LinkTypeDto;
import com.ppp.backend.service.LinkTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/linkType")
@RequiredArgsConstructor
@Slf4j
public class LinkTypeApiController {
    private final LinkTypeService linkTypeService;

    // ** 1.전체 링크 타입 조회(get)**
    @GetMapping("/list")
    public ResponseEntity<List<LinkTypeDto>> getAllLinkTypes(){
        log.info("getAllLinkTypes");
        List<LinkTypeDto> linkTypes = linkTypeService.getAllLinkTypes();
        return ResponseEntity.ok(linkTypes);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<LinkTypeDto> getLinkTypeById(@PathVariable Long id) {
        log.info("getLinkTypeById: {}", id);
        LinkTypeDto linkType = linkTypeService.getLinkTypeById(id);

        if (linkType == null) {
            log.warn("링크 타입이 존재하지 않습니다: {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(linkType);
    }

}

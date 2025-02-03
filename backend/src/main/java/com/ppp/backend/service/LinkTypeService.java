package com.ppp.backend.service;

import com.ppp.backend.domain.LinkType;
import com.ppp.backend.dto.LinkTypeDto;
import com.ppp.backend.repository.LinkTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LinkTypeService {

    private final LinkTypeRepository linkTypeRepository;

    // **1. 전체 링크 타입 조회**
    public List<LinkTypeDto> getAllLinkTypes() {
        return linkTypeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // **2. ID로 링크 타입 조회**
    public LinkTypeDto getLinkTypeById(Long id) {
        LinkType linkType = linkTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 링크 타입을 찾을 수 없습니다."));
        return toDto(linkType);
    }

    // **3. 새 링크 타입 생성**
    public LinkTypeDto createLinkType(LinkTypeDto linkTypeDto) {
        LinkType linkType = toEntity(linkTypeDto);
        LinkType savedLinkType = linkTypeRepository.save(linkType);
        return toDto(savedLinkType);
    }

    // **4. 기존 링크 타입 수정**
    public LinkTypeDto updateLinkType(Long id, LinkTypeDto linkTypeDto) {
        LinkType linkType = linkTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 링크 타입을 찾을 수 없습니다."));

        linkType.setName(linkTypeDto.getName());

        LinkType updatedLinkType = linkTypeRepository.save(linkType);
        return toDto(updatedLinkType);
    }

    // **5. 링크 타입 삭제**
    public void deleteLinkType(Long id) {
        if (!linkTypeRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 ID의 링크 타입을 찾을 수 없습니다.");
        }
        linkTypeRepository.deleteById(id);
    }

    // **Entity -> DTO 변환**
    private LinkTypeDto toDto(LinkType linkType) {
        return LinkTypeDto.builder()
                .id(linkType.getId())
                .name(linkType.getName())
                .build();
    }

    // **DTO -> Entity 변환**
    private LinkType toEntity(LinkTypeDto linkTypeDto) {
        return LinkType.builder()
                .name(linkTypeDto.getName())
                .build();
    }
}

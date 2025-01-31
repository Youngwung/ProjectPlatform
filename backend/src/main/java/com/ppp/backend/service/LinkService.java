package com.ppp.backend.service;

import com.ppp.backend.domain.Link;
import com.ppp.backend.domain.LinkType;
import com.ppp.backend.dto.LinkDto;
import com.ppp.backend.repository.LinkRepository;
import com.ppp.backend.repository.LinkTypeRepository;
import com.ppp.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LinkService {

    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final LinkTypeRepository linkTypeRepository;

    // **1. 전체 링크 조회**
    public List<LinkDto> getAllLinks() {
        return linkRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // **2. 특정 링크 조회**
    public LinkDto getLinkById(Long id) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 링크를 찾을 수 없습니다."));
        return toDto(link);
    }

    // **3. 새 링크 생성**
    public LinkDto createLink(LinkDto linkDto) {
        Link link = toEntity(linkDto);
        Link savedLink = linkRepository.save(link);
        return toDto(savedLink);
    }

    // **4. 링크 수정**
    public LinkDto updateLink(Long id, LinkDto linkDto) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 링크를 찾을 수 없습니다."));

        link.setUrl(linkDto.getUrl());
        link.setDescription(linkDto.getDescription());

        // 링크 타입 설정
        if (linkDto.getLinkTypeId() != null) {
            LinkType linkType = linkTypeRepository.findById(linkDto.getLinkTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 이름의 링크 타입을 찾을 수 없습니다."));
            link.setLinkType(linkType);
        }

        Link updatedLink = linkRepository.save(link);
        return toDto(updatedLink);
    }

    // **5. 링크 삭제**
    public void deleteLink(Long id) {
        if (!linkRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 ID의 링크를 찾을 수 없습니다.");
        }
        linkRepository.deleteById(id);
    }

    // **Entity -> DTO 변환**
    private LinkDto toDto(Link link) {
        Long linkTypeId = link.getLinkType().getId() != null ? link.getLinkType().getId() : null;

        return LinkDto.builder()
                .id(link.getId())
                .userId(link.getUser() != null ? link.getUser().getId() : 1L)
                .linkTypeId(linkTypeId) // 링크 타입 Id 설정
                .url(link.getUrl())
                .description(link.getDescription())
                .build();
    }

    // **DTO -> Entity 변환**
    private Link toEntity(LinkDto linkDto) {
        return Link.builder()
                .user(userRepository.findById(linkDto.getUserId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저를 찾을 수 없습니다.")))
                .linkType(linkTypeRepository.findById(linkDto.getLinkTypeId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 이름의 링크 타입을 찾을 수 없습니다.")))
                .url(linkDto.getUrl())
                .description(linkDto.getDescription())
                .build();
    }
}

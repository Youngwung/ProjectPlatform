package com.ppp.backend.service;

import com.ppp.backend.domain.Link;
import com.ppp.backend.domain.LinkType;
import com.ppp.backend.dto.LinkDto;
import com.ppp.backend.repository.LinkRepository;
import com.ppp.backend.repository.LinkTypeRepository;
import com.ppp.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
//@Transactional
public class LinkService {

    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final LinkTypeRepository linkTypeRepository;

    /* ========================= 🔹 조회 관련 메서드 🔹 ========================= */

    /** ✅ 전체 링크 조회 */
    public List<LinkDto> getAllLinks() {
        return linkRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** ✅ 특정 사용자의 모든 링크 조회 */
    public List<LinkDto> getUserLinks(Long userId) {
        return linkRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** ✅ 특정 링크 조회 */
    public LinkDto getLinkById(Long id) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 링크를 찾을 수 없습니다."));
        return toDto(link);
    }

    /* ========================= 🔹 생성 및 수정 관련 메서드 🔹 ========================= */

    /** ✅ 새 링크 생성 (기본 `linkTypeId` 설정 추가) */
    public LinkDto createLink(Long userId, LinkDto linkDto) {
        log.info("✅ 새 링크 생성 요청: userId={}, linkDto={}", userId, linkDto);

        // 만약 linkTypeId가 null이면 기본값 1L로 설정
        if (linkDto.getLinkTypeId() == null) {
            log.warn("⚠ linkTypeId가 null입니다. 기본값(1L)으로 설정");
            linkDto.setLinkTypeId(1L);  // 기본 linkTypeId 설정
        }
        
        // ★★★ description 필드 처리 및 로그 추가 ★★★
        // 클라이언트로부터 전달받은 description 값을 로그에 찍습니다.
        String description = linkDto.getDescription();
        log.info("클라이언트로부터 받은 description 값: {}", description);
        if (description == null) {
            log.info("description 값이 null입니다. 빈 문자열로 대체합니다.");
            description = "";
        } else {
            log.info("description 값이 존재합니다: {}", description);
        }

        // linkType 정보를 조회합니다.
        LinkType linkType = linkTypeRepository.findById(linkDto.getLinkTypeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 linkTypeId를 찾을 수 없습니다."));

        // Link 엔티티 생성 (URL, description, linkType 설정)
        Link link = Link.builder()
                .user(userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저를 찾을 수 없습니다.")))
                .url(linkDto.getUrl())
                .description(description)  // 로그에서 확인한 description 값을 저장
                .linkType(linkType)
                .build();

        // 엔티티를 저장 후 결과 로그 찍기
        Link savedLink = linkRepository.save(link);
        log.info("✅ 새 링크 추가 완료: userId={}, url={}, description={}", 
                userId, savedLink.getUrl(), savedLink.getDescription());
        return toDto(savedLink);
    }

    

    /** ✅ 특정 유저의 특정 링크 수정 (유저 검증 포함) */
    @Transactional
    public LinkDto updateUserLink(Long userId, Long linkId, LinkDto updatedLinkDto) {
        log.info("🔄 특정 링크 업데이트 요청: userId={}, linkId={}, updatedLinkDto={}", userId, linkId, updatedLinkDto);

        // ✅ 해당 링크가 존재하는지 확인
        Link link = linkRepository.findById(linkId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 링크를 찾을 수 없습니다. linkId=" + linkId));

        // ✅ 해당 유저의 링크인지 확인
        if (!link.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 링크는 해당 유저의 것이 아닙니다. userId=" + userId);
        }

        // ✅ 링크 데이터 업데이트
        link.setUrl(updatedLinkDto.getUrl());
        link.setDescription(updatedLinkDto.getDescription());

        // ✅ 링크 타입 변경 적용
        if (updatedLinkDto.getLinkTypeId() != null) {
            LinkType linkType = linkTypeRepository.findById(updatedLinkDto.getLinkTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 이름의 링크 타입을 찾을 수 없습니다."));
            link.setLinkType(linkType);
        }

        Link updatedLink = linkRepository.save(link);
        log.info("✅ 링크 업데이트 완료: linkId={}, url={}", updatedLink.getId(), updatedLink.getUrl());
        return toDto(updatedLink);
    }

    /** ✅ 사용자의 링크 업데이트 (링크 추가, 수정, 삭제 기능 포함) */
    @Transactional
    public void updateUserLinks(Long userId, List<LinkDto> links) {
        log.info("🔄 사용자 링크 업데이트 요청: userId={}", userId);

        if (userId == null) {
            throw new IllegalArgumentException("userId가 null일 수 없습니다.");
        }

        // ✅ 기존 링크 가져오기
        List<Link> existingLinks = linkRepository.findByUserId(userId);

        // ✅ 삭제할 링크 확인 (기존에는 있지만, 새 리스트에는 없는 링크 삭제)
        List<Long> newLinkIds = links.stream()
                .map(LinkDto::getId)
                .collect(Collectors.toList());

        List<Link> linksToDelete = existingLinks.stream()
                .filter(link -> !newLinkIds.contains(link.getId()))
                .collect(Collectors.toList());

        if (!linksToDelete.isEmpty()) {
            linksToDelete.forEach(link -> linkRepository.deleteById(link.getId()));
            log.info("🗑 삭제된 링크: {}", linksToDelete);
        }

        // ✅ 새 링크 추가 또는 기존 링크 수정
        for (LinkDto linkDto : links) {
            if (linkDto.getId() == null) {
                // 한 번 호출하고 결과를 변수에 저장
                LinkDto createdLink = createLink(userId, linkDto);
                log.info("createLink: {}", createdLink);
            } else {
                // 한 번 호출하고 결과를 변수에 저장
                LinkDto updatedLink = updateUserLink(userId, linkDto.getId(), linkDto);
                log.info("updateUserLink: {}", updatedLink);
            }
        }
        

        log.info("✅ 사용자 링크 업데이트 완료: userId={}", userId);
    }

    /* ========================= 🔹 삭제 관련 메서드 🔹 ========================= */

    /** ✅ 특정 링크 삭제 */
    @Transactional
    public void deleteUserLink(Long userId, Long linkId) {
        log.info("🗑 링크 삭제 요청: userId={}, linkId={}", userId, linkId);

        // ✅ 해당 링크가 존재하는지 확인
        Link link = linkRepository.findById(linkId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 링크를 찾을 수 없습니다. linkId=" + linkId));

        // ✅ 해당 유저의 링크인지 확인
        if (!link.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 링크는 해당 유저의 것이 아닙니다. userId=" + userId);
        }

        linkRepository.deleteById(linkId);
        log.info("✅ 링크 삭제 완료: linkId={}", linkId);
    }

    /* ========================= 🔹 변환 메서드 (Entity <-> DTO) 🔹 ========================= */

    /** ✅ Entity -> DTO 변환 */
    private LinkDto toDto(Link link) {
        Long linkTypeId = link.getLinkType() != null ? link.getLinkType().getId() : 1L;  // ✅ 기본값 설정

        return LinkDto.builder()
                .id(link.getId())
                .userId(link.getUser().getId())
                .linkTypeId(linkTypeId)
                .url(link.getUrl())
                .description(link.getDescription())
                .build();
    }
}

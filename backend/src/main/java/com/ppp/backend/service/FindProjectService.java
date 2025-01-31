package com.ppp.backend.service;

import com.ppp.backend.domain.Link;
import com.ppp.backend.domain.User;
import com.ppp.backend.dto.FindProjectDto;
import com.ppp.backend.domain.FindProject;
import com.ppp.backend.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FindProjectService {

    private final FindProjectRepository findProjectRepository;
    private final UserRepository userRepository;
    private final LinkRepository linkRepository;

    // **1. 전체 프로젝트 조회**
    public List<FindProjectDto> getAllFindProjects() {
        return findProjectRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // **2. 검색어로 프로젝트 조회**
    public List<FindProjectDto> searchFindProjects(String searchTerm) {
        return findProjectRepository.findByTitleContainingIgnoreCase(searchTerm).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // **3. ID로 프로젝트 상세 조회**
    public FindProjectDto getFindProjectById(Long id) {
        return findProjectRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 프로젝트를 찾을 수 없습니다."));
    }

    // **4. 새 프로젝트 생성**
    public FindProjectDto createFindProject(FindProjectDto findProjectDto) {
        User user = userRepository.findById(findProjectDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저를 찾을 수 없습니다."));

        // ✅ linkIds에 해당하는 Link 객체들을 조회
        List<Link> links = linkRepository.findAllById(findProjectDto.getLinkIds());

        // ✅ 프로젝트 저장
        FindProject project = FindProject.builder()
                .title(findProjectDto.getTitle())
                .description(findProjectDto.getDescription())
                .user(user)
                .links(links) // ✅ 여러 개의 Link 객체 저장
                .createdAt(findProjectDto.getCreatedAt())
                .updatedAt(findProjectDto.getUpdatedAt())
                .build();

        FindProject savedProject = findProjectRepository.save(project);
        return convertToDto(savedProject);
    }

    // **5. 기존 프로젝트 수정**
    public FindProjectDto updateFindProject(Long id, FindProjectDto findProjectDto) {
        FindProject project = findProjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 프로젝트를 찾을 수 없습니다."));

        // ✅ linkIds를 기반으로 Link 객체 리스트 조회
        List<Link> updatedLinks = linkRepository.findAllById(findProjectDto.getLinkIds());

        project.setTitle(findProjectDto.getTitle());
        project.setDescription(findProjectDto.getDescription());
        project.setLinks(updatedLinks); // ✅ 링크 리스트 업데이트

        FindProject updatedProject = findProjectRepository.save(project);
        return convertToDto(updatedProject);
    }

    // **6. 프로젝트 삭제**
    public void deleteFindProject(Long id) {
        if (!findProjectRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 ID의 프로젝트를 찾을 수 없습니다.");
        }
        findProjectRepository.deleteById(id);
    }

    // **Entity -> DTO 변환**
    private FindProjectDto convertToDto(FindProject project) {
        return FindProjectDto.builder()
                .id(project.getId())
                .userId(project.getUser().getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .linkIds(project.getLinks().stream().map(Link::getId).collect(Collectors.toList()))
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    // **DTO -> Entity 변환**
    private FindProject convertToEntity(FindProjectDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저를 찾을 수 없습니다."));

        List<Link> links = linkRepository.findAllById(dto.getLinkIds()); // ✅ 링크 리스트 조회

        return FindProject.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .user(user)
                .links(links) // ✅ 링크 리스트 저장
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}

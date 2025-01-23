package com.ppp.backend.service;

import com.ppp.backend.domain.User;
import com.ppp.backend.dto.FindProjectDto;
import com.ppp.backend.domain.FindProject;
import com.ppp.backend.repository.FindProjectRepository;
import com.ppp.backend.repository.UserRepository;

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
        FindProject project = convertToEntity(findProjectDto);
        FindProject savedProject = findProjectRepository.save(project);
        return convertToDto(savedProject);
    }

    // **5. 기존 프로젝트 수정**
    public FindProjectDto updateFindProject(Long id, FindProjectDto findProjectDto) {
        FindProject project = findProjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 프로젝트를 찾을 수 없습니다."));
        project.setTitle(findProjectDto.getTitle());
        project.setDescription(findProjectDto.getDescription());
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
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    // **DTO -> Entity 변환**
    private FindProject convertToEntity(FindProjectDto dto) {
        User user = userRepository.findById(dto.getUserId()) // userId로 User 조회
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저를 찾을 수 없습니다."));

        return FindProject.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .user(user)
                .description(dto.getDescription())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}

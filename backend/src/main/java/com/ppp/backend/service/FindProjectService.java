package com.ppp.backend.service;

import com.ppp.backend.domain.FindProject;
import com.ppp.backend.domain.User;
import com.ppp.backend.dto.FindProjectDto;
import com.ppp.backend.repository.FindProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FindProjectService {

    private final FindProjectRepository findProjectRepository;

    @Autowired
    public FindProjectService(FindProjectRepository findProjectRepository) {
        this.findProjectRepository = findProjectRepository;
    }

    // 모든 프로젝트 가져오기
    public List<FindProjectDto> getAllProjects() {
        List<FindProject> projects = findProjectRepository.findAll();
        return projects.stream().map(this::toDto).collect(Collectors.toList());
    }

    // 특정 프로젝트 ID로 가져오기
    public FindProjectDto getProjectById(Long id) {
        Optional<FindProject> project = findProjectRepository.findById(id);
        return project.map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));
    }

    // 특정 사용자의 프로젝트 가져오기
    public List<FindProjectDto> getProjectsByUserId(Long userId) {
        List<FindProject> projects = findProjectRepository.findByUserId(userId);
        return projects.stream().map(this::toDto).collect(Collectors.toList());
    }

    // 프로젝트 생성 또는 업데이트
//    public FindProjectDto saveOrUpdateProject(FindProjectDto findProjectDto) {
//        FindProject project = toEntity(findProjectDto);
//        FindProject savedProject = findProjectRepository.save(project);
//        return toDto(savedProject);
//    }

    // 프로젝트 삭제
    public void deleteProject(Long id) {
        findProjectRepository.deleteById(id);
    }

    // DTO -> Entity 변환
//    private FindProject toEntity(FindProjectDto dto) {
//        return FindProject.builder()
//                .id(dto.getId())
//                .user(new User(dto.getUserId())) // User 객체 생성, ID만 설정
//                .title(dto.getTitle())
//                .description(dto.getDescription())
//                .build();
//    }

    // Entity -> DTO 변환
    private FindProjectDto toDto(FindProject project) {
        return new FindProjectDto(
                project.getId(),
                project.getUser().getId(), // User의 ID를 가져옴
                project.getTitle(),
                project.getDescription()
        );
    }
}

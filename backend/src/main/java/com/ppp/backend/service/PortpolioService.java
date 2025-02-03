package com.ppp.backend.service;

import com.ppp.backend.domain.Link;
import com.ppp.backend.domain.User;
import com.ppp.backend.dto.PortpolioDto;
import com.ppp.backend.domain.Portpolio;
import com.ppp.backend.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PortpolioService {

    private final PortpolioRepository PortpolioRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final LinkRepository linkRepository;

    // **1. 전체 프로젝트 조회**
    public List<PortpolioDto> getAllPortpolios() {
        return PortpolioRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // **2. 검색어로 프로젝트 조회**
    public List<PortpolioDto> searchPortpolios(String searchTerm) {
        return PortpolioRepository.findByTitleContainingIgnoreCase(searchTerm).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // **3. ID로 프로젝트 상세 조회**
    public PortpolioDto getPortpolioById(Long id) {
        return PortpolioRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 프로젝트를 찾을 수 없습니다."));
    }

    // **4. 새 프로젝트 생성**
    public PortpolioDto createPortpolio(PortpolioDto PortpolioDto) {
        Portpolio project = convertToEntity(PortpolioDto);
        Portpolio savedProject = PortpolioRepository.save(project);
        return convertToDto(savedProject);
    }

    // **5. 기존 프로젝트 수정**
    public PortpolioDto updatePortpolio(Long id, PortpolioDto PortpolioDto) {
        Portpolio project = PortpolioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 프로젝트를 찾을 수 없습니다."));

        project.setTitle(PortpolioDto.getTitle());
        project.setDescription(PortpolioDto.getDescription());

        Portpolio updatedProject = PortpolioRepository.save(project);
        return convertToDto(updatedProject);
    }

    // **6. 프로젝트 삭제**
    public void deletePortpolio(Long id) {
        if (!PortpolioRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 ID의 프로젝트를 찾을 수 없습니다.");
        }
        PortpolioRepository.deleteById(id);
    }

    // **Entity -> DTO 변환**
    private PortpolioDto convertToDto(Portpolio project) {
        return PortpolioDto.builder()
                .id(project.getId())
                .userId(project.getUser().getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    // **DTO -> Entity 변환**
    private Portpolio convertToEntity(PortpolioDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저를 찾을 수 없습니다."));

        return Portpolio.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .user(user)
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}

package com.ppp.backend.service;

import com.ppp.backend.domain.Portfolio;
import com.ppp.backend.domain.User;
import com.ppp.backend.dto.PortfolioDto;
import com.ppp.backend.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final LinkRepository linkRepository;

    // **1. 전체 프로젝트 조회**
    public List<PortfolioDto> getAllPortfolios() {
        return portfolioRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // **2. 검색어로 프로젝트 조회**
    public List<PortfolioDto> searchPortfolios(String searchTerm) {
        return portfolioRepository.findByTitleContainingIgnoreCase(searchTerm).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // **3. ID로 프로젝트 상세 조회**
    public PortfolioDto getPortfolioById(Long id) {
            return portfolioRepository.findById(id)
                    .map(this::convertToDto)
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID의 프로젝트를 찾을 수 없습니다."));
        }

    // **4. 새 프로젝트 생성**
    public PortfolioDto createPortfolio(PortfolioDto portfolioDto,Long userId) {

            portfolioDto.setUserId(userId);

            Portfolio project = convertToEntity(portfolioDto);
            Portfolio savedProject = portfolioRepository.save(project);
            log.info("save pppp{}",savedProject);
            return convertToDto(savedProject);
        }

    // **5. 기존 프로젝트 수정**
    public PortfolioDto updatePortfolio(Long id, PortfolioDto portfolioDto) {
            Portfolio project = portfolioRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID의 프로젝트를 찾을 수 없습니다."));
    
            project.setTitle(portfolioDto.getTitle());
            project.setDescription(portfolioDto.getDescription());
    
            Portfolio updatedProject = portfolioRepository.save(project);
            return convertToDto(updatedProject);
        }

    // **6. 프로젝트 삭제**
    public void deletePortfolio(Long id) {
        if (!portfolioRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 ID의 프로젝트를 찾을 수 없습니다.");
        }
        portfolioRepository.deleteById(id);
    }

    // **Entity -> DTO 변환**
    private PortfolioDto convertToDto(Portfolio project) {
            return PortfolioDto.builder()
                    .id(project.getId())
                    .userId(project.getUser().getId())
                    .title(project.getTitle())
                    .description(project.getDescription())
                    .createdAt(project.getCreatedAt())
                    .updatedAt(project.getUpdatedAt())
                    .build();
        }

    // **DTO -> Entity 변환**
    private Portfolio convertToEntity(PortfolioDto dto) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저를 찾을 수 없습니다."));

    
            return Portfolio.builder()
                    .id(dto.getId())
                    .title(dto.getTitle())
                    .description(dto.getDescription())
                    .user(user)
                    .createdAt(dto.getCreatedAt())
                    .updatedAt(dto.getUpdatedAt())
                    .build();
        }
}

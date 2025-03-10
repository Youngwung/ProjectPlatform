package com.ppp.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ppp.backend.domain.Portfolio;
import com.ppp.backend.domain.Skill;
import com.ppp.backend.domain.SkillLevel;
import com.ppp.backend.domain.User;
import com.ppp.backend.domain.UserSkill;
import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
import com.ppp.backend.dto.PortfolioDto;
import com.ppp.backend.repository.PortfolioRepository;
import com.ppp.backend.repository.SkillLevelRepository;
import com.ppp.backend.repository.SkillRepository;
import com.ppp.backend.repository.UserRepository;
import com.ppp.backend.repository.UserSkillRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PortfolioService extends AbstractSkillService<UserSkill, PortfolioDto, UserSkillRepository, User> {
        private final PortfolioRepository portfolioRepository;
        private final UserRepository userRepository;

        public PortfolioService(
                        UserSkillRepository repository,
                        SkillRepository skillRepo,
                        SkillLevelRepository skillLevelRepo,
                        PortfolioRepository portfolioRepo,
                        UserRepository userRepo) {
                super(repository, skillRepo, skillLevelRepo);
                this.portfolioRepository = portfolioRepo;
                this.userRepository = userRepo;
        }

        // **1. 전체 프로젝트 조회**
        public PageResponseDTO<PortfolioDto> getAllPortfolios(PageRequestDTO pageRequestDTO) {
                Page<Portfolio> result = portfolioRepository.searchString(PageRequestDTO.builder().build());
                List<PortfolioDto> dtoList = result.get().map((portfolio) -> {
                        PortfolioDto dto = convertToDto(portfolio);
                        String skillString = getSkill(portfolio.getUser().getId());
                        dto.setSkills(skillString);
                        return dto;
                }).collect(Collectors.toList());

                PageResponseDTO<PortfolioDto> pageResponseDTO = new PageResponseDTO<>(dtoList, pageRequestDTO,
                                result.getTotalElements());

                return pageResponseDTO;
        }

        // **2. 검색어로 프로젝트 조회**
        public List<PortfolioDto> searchPortfolios(String searchTerm) {
                return portfolioRepository.findByTitleContainingIgnoreCase(searchTerm).stream()
                                .map((entity) -> {
                                        PortfolioDto dto = convertToDto(entity);
                                        dto.setSkills(getSkill(entity.getUser().getId()));
                                        return dto;
                                })
                                .collect(Collectors.toList());
        }

        // **3. ID로 프로젝트 상세 조회**
        public PortfolioDto getPortfolioById(Long id) {
                return portfolioRepository.findById(id)
                                .map((entity) -> {
                                        PortfolioDto dto = convertToDto(entity);
                                        dto.setSkills(getSkill(entity.getUser().getId()));
                                        return dto;
                                })
                                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 프로젝트를 찾을 수 없습니다."));
        }

        // **4. 새 프로젝트 생성**
        public PortfolioDto createPortfolio(PortfolioDto portfolioDto, Long userId) {
                User user = userRepository.findById(userId).orElse(null);
                portfolioDto.setUserName(user.getName());
                Portfolio portfolio = convertToEntity(portfolioDto);
                Portfolio savedPortfolio = portfolioRepository.save(portfolio);
                log.info("save pppp{}", savedPortfolio);
                return convertToDto(savedPortfolio);
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

        public PageResponseDTO<PortfolioDto> getSearchResult(PageRequestDTO pageRequestDTO) {
                Page<Portfolio> result = portfolioRepository.searchKeyword(pageRequestDTO);
                // ! Portfolio List => PortfolioDTO List
                List<PortfolioDto> dtoList = result.get().map(portfolio -> {
                        PortfolioDto dto = convertToDto(portfolio);
                        String skillString = getSkill(portfolio.getUser().getId());
                        dto.setSkills(skillString);

                        return dto;
                }).collect(Collectors.toList());

                // 페이징 처리
                PageResponseDTO<PortfolioDto> pageResponseDTO = new PageResponseDTO<>(dtoList, pageRequestDTO,
                                result.getTotalElements());

                return pageResponseDTO;
        }

        // **Entity -> DTO 변환**
        private PortfolioDto convertToDto(Portfolio project) {
                return PortfolioDto.builder()
                                .id(project.getId())
                                .userName(project.getUser().getName())
                                .title(project.getTitle())
                                .description(project.getDescription())
                                .createdAt(project.getCreatedAt())
                                .updatedAt(project.getUpdatedAt())
                                .build();
        }

        // **DTO -> Entity 변환**
        private Portfolio convertToEntity(PortfolioDto dto) {
                log.info("email = {}", dto.getEmail());
                User user = userRepository.findByEmail(dto.getEmail())
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

        @Override
        UserSkill createSkillInstance(Long id, User parentEntity, Skill skill, SkillLevel skillLevel) {
                return UserSkill.builder()
                                .id(id)
                                .user(parentEntity)
                                .skill(skill)
                                .skillLevel(skillLevel)
                                .build();
        }

        public List<PortfolioDto> getMyPortfolios(Long userId) {
                List<Portfolio> portfolios = portfolioRepository.findByUserId(userId);
                return portfolios.stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList());
        }

        public boolean checkPortfolioWriter(Long userId, Long portfolioId) {
                log.info("portfolioId = {}, userId = {}", portfolioId, userId);
                Long writer = portfolioRepository.findById(portfolioId).orElseThrow().getUser().getId();
                return userId == writer;
        }

}

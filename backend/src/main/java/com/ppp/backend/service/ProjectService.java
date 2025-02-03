package com.ppp.backend.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.ppp.backend.domain.Project;
import com.ppp.backend.domain.User;
import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
import com.ppp.backend.dto.ProjectDTO;
import com.ppp.backend.repository.UserRepository;
import com.ppp.backend.status.ProjectStatus;

import jakarta.transaction.Transactional;

@Transactional
public interface ProjectService {

	ProjectDTO get(Long jPno);

	Long register(ProjectDTO dto);

	void modify(ProjectDTO dto);

	void remove(Long jpNo);

	PageResponseDTO<ProjectDTO> getList(PageRequestDTO PageRequestDTO);

	// Entity → DTO 변환
	default ProjectDTO fromEntity(Project entity) {
		if (entity == null) {
			return null;
		}

		ProjectDTO dto = ProjectDTO.builder()
				.id(entity.getId())
				.userId(entity.getUser().getId()) // User 객체에서 ID 추출
				.title(entity.getTitle())
				.description(entity.getDescription())
				.maxPeople(entity.getMaxPeople())
				.isPublic(entity.isPublic())
				.status(entity.getStatus().toString())
				.createdAt(entity.getCreatedAt().toLocalDateTime())
				.updatedAt(entity.getUpdatedAt().toLocalDateTime())
				.build();

		// 만약에 스테이터스가 null이면 default값
		// update 시 status가 항상 null로 초기화 되는 문제 해결을 위한 코드
		if (dto.getStatus() == null) {
			dto.setStatus("모집_중");
		}

		return dto;
	}

	// DTO → Entity 변환
	default Project toEntity(ProjectDTO dto, UserRepository userRepository) {
		if (dto == null) {
			return null;
		}

		User user = userRepository.findById(dto.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + dto.getUserId()));

		// 만약에 스테이터스가 null이면 default값
		// update 시 status가 항상 null로 초기화 되는 문제 해결을 위한 코드
		if (dto.getStatus() == null) {
			dto.setStatus("모집_중");
		}

		Project entity = Project.builder()
				.id(dto.getId())
				.user(user) // User 객체를 직접 설정
				.title(dto.getTitle())
				.description(dto.getDescription())
				.maxPeople(dto.getMaxPeople())
				.status(ProjectStatus.valueOf(dto.getStatus()))
				.isPublic(dto.isPublic())
				.createdAt(dto.getCreatedAt() == null ? Timestamp.valueOf(LocalDateTime.now()) : Timestamp.valueOf(dto.getCreatedAt()))
				.updatedAt(dto.getUpdatedAt() == null ? Timestamp.valueOf(LocalDateTime.now()) : Timestamp.valueOf(dto.getUpdatedAt()))
				.build();

		return entity;
	}

}

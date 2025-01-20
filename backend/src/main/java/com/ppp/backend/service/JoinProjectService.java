package com.ppp.backend.service;

import com.ppp.backend.domain.JoinProject;
import com.ppp.backend.domain.User;
import com.ppp.backend.dto.JoinProjectDTO;
import com.ppp.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Transactional
public interface JoinProjectService {

	JoinProjectDTO get(Long jPno);

	Long register(JoinProjectDTO dto);

	void modify(JoinProjectDTO dto);

	void remove(JoinProjectDTO dto);
	
	// Entity → DTO 변환
	default JoinProjectDTO fromEntity(JoinProject entity) {
		if (entity == null) {
			return null;
		}
		return JoinProjectDTO.builder()
				.id(entity.getId())
				.userId(entity.getUser().getId()) // User 객체에서 ID 추출
				.title(entity.getTitle())
				.description(entity.getDescription())
				.maxPeople(entity.getMaxPeople())
				.status(entity.getStatus())
				.isPublic(entity.isPublic())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();
	}

	// DTO → Entity 변환
	default JoinProject toEntity(JoinProjectDTO dto, UserRepository userRepository) {
		if (dto == null) {
			return null;
		}

		User user = userRepository.findById(dto.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + dto.getUserId()));

		return JoinProject.builder()
				.id(dto.getId())
				.user(user) // User 객체를 직접 설정
				.title(dto.getTitle())
				.description(dto.getDescription())
				.maxPeople(dto.getMaxPeople())
				.status(dto.getStatus())
				.isPublic(dto.isPublic())
				.createdAt(dto.getCreatedAt())
				.updatedAt(dto.getUpdatedAt())
				.build();
	}

}

package com.ppp.backend.service;

import org.springframework.stereotype.Service;

import com.ppp.backend.domain.JoinProject;
import com.ppp.backend.dto.JoinProjectDTO;
import com.ppp.backend.repository.JoinProjectRepository;
import com.ppp.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class JoinProjectServiceImpl implements JoinProjectService{
	private final JoinProjectRepository jPRepo;
	private final UserRepository userRepo;

	@Override
	public JoinProjectDTO get(Long jPNo) {
		JoinProject jProject = jPRepo.findById(jPNo).orElseThrow();
		return fromEntity(jProject);
	}

	@Override
	public Long register(JoinProjectDTO dto) {
		JoinProjectDTO jPDTO = JoinProjectDTO.builder()
			.userId(dto.getUserId())
			.title(dto.getTitle())
			.description(dto.getDescription())
			.maxPeople(dto.getMaxPeople())
			.isPublic(dto.isPublic())
		.build();
		JoinProject jProject = toEntity(jPDTO, userRepo);
		JoinProject result = jPRepo.save(jProject);

		// 등록된 프로젝트 번호 리턴
		return result.getId();
	}

	@Override
	public void modify(JoinProjectDTO dto) {

	}

	@Override
	public void remove(JoinProjectDTO dto) {

	}
}

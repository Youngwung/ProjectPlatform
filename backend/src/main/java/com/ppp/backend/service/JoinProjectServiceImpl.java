package com.ppp.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ppp.backend.domain.JoinProject;
import com.ppp.backend.dto.JoinProjectDTO;
import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
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
		JoinProject jProject = toEntity(dto, userRepo);
		JoinProject result = jPRepo.save(jProject);
		// 등록된 프로젝트 번호 리턴
		return result.getId();
	}

	@Override
	public void modify(JoinProjectDTO dto) {
		// 반드시 업데이트 메서드 작성 시 기존 엔터티를 가져올 필요는 없지만
		// 업데이트 시 공개 여부 변수(isPublic)이 false로 초기화되는 문제를 해결하기 위해
		// 기존 엔터티의 데이터를 가져와서 삽입해 주었음.
		JoinProject entity = jPRepo.findById(dto.getId()).orElseThrow();
		dto.setPublic(entity.isPublic());

		JoinProject joinProject = toEntity(dto, userRepo);
		jPRepo.save(joinProject);
	}

	@Override
	public void remove(Long jpNo) {
		jPRepo.deleteById(jpNo);
	}

	@Override
	public PageResponseDTO<JoinProjectDTO> getList(PageRequestDTO PageRequestDTO) {
		// JPA
		Page<JoinProject> result = jPRepo.search1(PageRequestDTO);
		
		//! JoinProject List => JoinProjectDTO List
		List<JoinProjectDTO> dtoList = result.get().map(joinProject -> fromEntity(joinProject)).collect(Collectors.toList());

		PageResponseDTO<JoinProjectDTO> pageResponseDTO = new PageResponseDTO<>(dtoList, PageRequestDTO, result.getTotalElements());
		
		return pageResponseDTO;
	}
}

package com.ppp.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ppp.backend.domain.JoinProject;
import com.ppp.backend.domain.JoinProjectSkill;
import com.ppp.backend.domain.Skill;
import com.ppp.backend.domain.SkillLevel;
import com.ppp.backend.dto.JoinProjectDTO;
import com.ppp.backend.dto.PageRequestDTO;
import com.ppp.backend.dto.PageResponseDTO;
import com.ppp.backend.repository.JoinProjectRepository;
import com.ppp.backend.repository.JoinProjectSkillRepository;
import com.ppp.backend.repository.SkillLevelRepository;
import com.ppp.backend.repository.SkillRepository;
import com.ppp.backend.repository.UserRepository;
import com.ppp.backend.util.SkillDtoConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class JoinProjectServiceImpl implements JoinProjectService {
	private final JoinProjectRepository jPRepo;
	private final UserRepository userRepo;
	private final SkillRepository skillRepo;
	private final SkillLevelRepository skillLevelRepo;
	private final JoinProjectSkillRepository joinProjectSkillRepo;

	@Override
	public JoinProjectDTO get(Long jPNo) {
		JoinProject jProject = jPRepo.findById(jPNo).orElseThrow();
		JoinProjectDTO dto = fromEntity(jProject);

		// TODO: DB => 정규 표현식 변환 메서드 작성 후 구현
		// dto.setSkills(null);
		return dto;
	}

	@Override
	public Long register(JoinProjectDTO dto) {
		JoinProject jProject = toEntity(dto, userRepo);
		
		// !등록된 글 번호로 JoinProjectSkill 객체를 DB에 등록
		// 정규 표현식으로 작성된 문자열
		String skills = dto.getSkills();
		
		// 문자열을 Map 형식으로 변환
		Map<String, String> skillMap = SkillDtoConverter.convertSkillDtoToMap(skills);
		
		// 프론트엔드에서 api호출로 유효성 검사를 하지만 여기에서도 한번 더 검사
		
		// 유효성 검사 리스트
		List<String> invalidList = new ArrayList<>();
		List<JoinProjectSkill> joinProjectSkills = new ArrayList<>();
		
		skillMap.forEach((key, value) -> {
			Skill skill = skillRepo.findByNameIgnoreCase(key);
			SkillLevel skillLevel = skillLevelRepo.findByNameIgnoreCase(value);
			if (skill == null) {
				invalidList.add("Invalid skill: " + key);
			}
			if (skillLevel == null) {
				invalidList.add("Invalid skill level: " + value);
			}
			// 유효한 데이터인 경우 JoinProjectSkill 객체 생성 후 리스트에 추가
			JoinProjectSkill joinProjectSkill = JoinProjectSkill.builder()
				.joinProject(jProject)
				.skill(skill)
				.skillLevel(skillLevel)
			.build();

			joinProjectSkills.add(joinProjectSkill);
		});
		
		// 유효하지 않은 데이터가 있는 경우
		if (!invalidList.isEmpty()) {
			// 예외 처리: -1을 리턴하여 등록 실패를 클라이언트에 알림
			return -1L;
		}
		
		// 유효성 검사를 통과한 경우 프로젝트 데이터 저장
		JoinProject result = jPRepo.save(jProject);

		// Skill과 SkillLevel을 joinProjectSkill 테이블에 저장
		joinProjectSkills.forEach((joinProject) -> {
			joinProjectSkillRepo.save(joinProject);
		});
		
		// 등록된 프로젝트 번호 리턴
		return result.getId();
	}

	@Override
	public void modify(JoinProjectDTO dto) {
		// TODO: Skills 관련 로직 추가
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
		// TODO: Skills 관련 로직 추가
		// JPA
		Page<JoinProject> result = jPRepo.search1(PageRequestDTO);

		// ! JoinProject List => JoinProjectDTO List
		List<JoinProjectDTO> dtoList = result.get().map(joinProject -> fromEntity(joinProject))
				.collect(Collectors.toList());

		PageResponseDTO<JoinProjectDTO> pageResponseDTO = new PageResponseDTO<>(dtoList, PageRequestDTO,
				result.getTotalElements());

		return pageResponseDTO;
	}

	@Override
	public boolean isSkillExist(String skillName) {
		Map<String, String> skillMap = SkillDtoConverter.convertSkillDtoToMap(skillName);

		return false;
	}
}

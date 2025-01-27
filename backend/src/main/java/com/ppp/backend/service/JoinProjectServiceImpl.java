package com.ppp.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class JoinProjectServiceImpl implements JoinProjectService {
	private final JoinProjectRepository jPRepo;
	private final UserRepository userRepo;
	private final SkillRepository skillRepo;
	private final SkillLevelRepository skillLevelRepo;
	private final JoinProjectSkillRepository joinProjectSkillRepo;
	private final SkillDtoConverter skillDtoConverter;

	@Override
	public JoinProjectDTO get(Long jPNo) {
		JoinProject jProject = jPRepo.findById(jPNo).orElseThrow();
		JoinProjectDTO dto = fromEntity(jProject);

		List<JoinProjectSkill> jPSkills = joinProjectSkillRepo.findByJoinProjectId(jPNo);
		Map<String, String> jPSkillMap = new HashMap<>();
		jPSkills.forEach(jPSkill -> {
			jPSkillMap.put(jPSkill.getSkill().getName(), jPSkill.getSkillLevel().getName());
		});

		String skillString = skillDtoConverter.convertMapToSkillDto(jPSkillMap);
		dto.setSkills(skillString);
		return dto;
	}

	@Override
	public Long register(JoinProjectDTO dto) {
		JoinProject jProject = toEntity(dto, userRepo);

		// !등록된 글 번호로 JoinProjectSkill 객체를 DB에 등록
		// 정규 표현식으로 작성된 문자열
		String skills = dto.getSkills();

		// 문자열을 Map 형식으로 변환
		Map<String, String> skillMap = skillDtoConverter.convertSkillDtoToMap(skills);

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
		// 반드시 업데이트 메서드 작성 시 기존 엔터티를 가져올 필요는 없지만
		// 업데이트 시 공개 여부 변수(isPublic)이 false로 초기화되는 문제를 해결하기 위해
		// 기존 엔터티의 데이터를 가져와서 삽입해 주었음.
		JoinProject entity = jPRepo.findById(dto.getId()).orElseThrow();
		dto.setPublic(entity.isPublic());

		JoinProject joinProject = toEntity(dto, userRepo);
		jPRepo.save(joinProject);

		// 유효성 검사 리스트
		List<String> invalidList = new ArrayList<>();

		// dto의 스킬이 수정되었는 지 검사
		String dtoSkills = dto.getSkills();
		Map<String, String> dtoSkillMap = skillDtoConverter.convertSkillDtoToMap(dtoSkills);

		// 현재 스킬맵의 각 항목에 대해 검사
		dtoSkillMap.forEach((key, value) -> {
			Skill skill = skillRepo.findByNameIgnoreCase(key);
			SkillLevel skillLevel = skillLevelRepo.findByNameIgnoreCase(value);

			// 받아온 스킬과 숙련도가 DB에 존재하는 지 검사
			if (skill == null) {
				invalidList.add("Invalid skill: " + key);
			}
			if (skillLevel == null) {
				invalidList.add("Invalid skill level: " + value);
			}
		});

		// 유효하지 않은 데이터가 있는 경우
		if (!invalidList.isEmpty()) {
			// TODO: 예외 처리 필요
		}

		// 기존 스킬 리스트를 가져옴
		List<JoinProjectSkill> existingSkills = joinProjectSkillRepo.findByJoinProjectId(dto.getId());
		log.info("existingSkills = {}", existingSkills);

		List<JoinProjectSkill> updateList = new ArrayList<>();
		// 기존 스킬로 초기화
		List<JoinProjectSkill> removeList = new ArrayList<>();

		// 기존 스킬 리스트를 순회하면서 비교
		existingSkills.forEach(existingSkill -> {
			String skillName = existingSkill.getSkill().getName();
			String skillLevel = existingSkill.getSkillLevel().getName();

			// 새로운 스킬 맵에 해당 스킬이 있는지 확인
			if (dtoSkillMap.containsKey(skillName)) {
				String newLevel = dtoSkillMap.get(skillName);
				if (!skillLevel.equals(newLevel)) {
					// 레벨이 다른 경우 객체를 생성해 updateList에 추가
					SkillLevel newLevelObj = skillLevelRepo.findByNameIgnoreCase(newLevel);
					JoinProjectSkill updateSkill = JoinProjectSkill.builder()
					// 이 경우 update이므로 고유 키를 명시해서 객체를 생성
							.id(existingSkill.getId())
							.joinProject(joinProject)
							.skill(existingSkill.getSkill())
							.skillLevel(newLevelObj)
						.build();
					updateList.add(updateSkill);
				}
				// 비교가 끝난 스킬은 새로운 스킬 맵에서 제거
				dtoSkillMap.remove(skillName);
			} else {
				// 새로운 스킬 맵에 없는 경우 removeList에 추가
				removeList.add(existingSkill);
			}
		});

		// 새로운 스킬 맵에 남아있는 스킬들은 새로 추가될 스킬들
		if (!dtoSkillMap.isEmpty()) {
			dtoSkillMap.forEach((skillName, skillLevel) -> {
				Skill newSkill = skillRepo.findByNameIgnoreCase(skillName);
				SkillLevel newLevel = skillLevelRepo.findByNameIgnoreCase(skillLevel);

				// update가 아니라 insert이므로 번호를 안 넣어줘도 됨.
				JoinProjectSkill skillObj = JoinProjectSkill.builder()
						.joinProject(joinProject)
						.skill(newSkill)
						.skillLevel(newLevel)
					.build();
				updateList.add(skillObj);
			});
		}

		// log.info("removeList = {}", removeList);
		// log.info("updateList = {}", updateList);
		// 제거 리스트에 있는 객체를 제거
		removeList.forEach(remove -> {
			joinProjectSkillRepo.delete(remove);
		});
		// 업데이트 리스트에 있는 객체를 추가
		updateList.forEach(update -> {
			joinProjectSkillRepo.save(update);
		});

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
		Map<String, String> skillMap = skillDtoConverter.convertSkillDtoToMap(skillName);

		return false;
	}
}

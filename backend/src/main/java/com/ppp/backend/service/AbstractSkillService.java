package com.ppp.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ppp.backend.domain.Skill;
import com.ppp.backend.domain.SkillLevel;
import com.ppp.backend.domain.skill.BaseSkill;
import com.ppp.backend.dto.skill.BaseSkillDto;
import com.ppp.backend.repository.BaseSkillRepository;
import com.ppp.backend.repository.SkillLevelRepository;
import com.ppp.backend.repository.SkillRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public abstract class AbstractSkillService<T extends BaseSkill, D extends BaseSkillDto, R extends BaseSkillRepository<T>, P>
		implements SkillService<T, D, P> {

	// ProjectSkillRepository 또는 UserSkillRepository
	// 자식 클래스에서 생성자 호출
	final R repository;
	final SkillRepository skillRepo;
	final SkillLevelRepository skillLevelRepo;

	/**
	 * @return dto skills를 초기화하는 문자열 리턴
	 */
	public String getSkill(Long ownerId) {
		List<T> existingSkills = repository.findByOwner(ownerId);

		Map<String, String> skillMap = new HashMap<>();
		existingSkills.forEach(Skill -> {
			skillMap.put(Skill.getSkill().getName(), Skill.getSkillLevel().getName());
		});

		String skills = convertMapToString(skillMap);

		return skills;
	}

	/**
	 * projectId or userId와 skills로 BaseSkill를 상속받는 객체 생성하는 메서드
	 * 
	 * @param id, skills
	 * @return Entity
	 */
	abstract T createSkillInstance(Long id, P parentEntity, Skill skill, SkillLevel skillLevel);

	/**
	 * 입력받은 스킬이 DB에서 관리하는 스킬인 지 검사하는 메서드
	 * 
	 * @param skills
	 * @return
	 */
	public boolean existingSkill(String skills) {
		// 프론트엔드에서 api호출로 유효성 검사를 하지만 여기에서도 한번 더 검사

		// 문자열을 Map 형식으로 변환
		Map<String, String> skillMap = convertStringToMap(skills);

		// 유효성 검사 리스트
		List<String> invalidList = new ArrayList<>();

		skillMap.forEach((key, value) -> {
			Skill skill = skillRepo.findByNameIgnoreCase(key);
			SkillLevel skillLevel = skillLevelRepo.findByNameIgnoreCase(value);

			if (skill == null) {
				invalidList.add(key);
			}
			if (skillLevel == null) {
				invalidList.add(value);
			}
		});

		// 유효하지 않은 데이터가 있는 경우
		if (!invalidList.isEmpty()) {
			// 예외 처리: -1을 리턴하여 등록 실패를 클라이언트에 알림
			return false;
		}

		return true;
	}

	@Override
	public void modifySkill(Long id, D dto, P parentEntity) {
		// dto의 스킬이 수정되었는 지 검사
		String dtoSkills = dto.getSkills();
		Map<String, String> dtoSkillMap = convertStringToMap(dtoSkills);

		// 기존 스킬 리스트를 가져옴
		List<T> existingSkills = repository.findByOwner(id);
		log.info("existingSkills = {}", existingSkills);

		List<T> updateList = new ArrayList<>();
		// 기존 스킬로 초기화
		List<T> removeList = new ArrayList<>();

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
					T updateSkill = createSkillInstance(id, parentEntity, existingSkill.getSkill(), newLevelObj);
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
				T insertSkill = createSkillInstance(null, parentEntity, newSkill, newLevel);
				updateList.add(insertSkill);
			});
		}

		// log.info("removeList = {}", removeList);
		// log.info("updateList = {}", updateList);
		// 제거 리스트에 있는 객체를 제거
		removeList.forEach(remove -> {
			repository.delete(remove);
		});
		// 업데이트 리스트에 있는 객체를 추가
		// 기존 엔터티의 값을 수정
		existingSkills.forEach(existingSkill -> {
			updateList.forEach(updateSkill -> {
				if (existingSkill.equals(updateSkill)) {
					existingSkill.setSkill(updateSkill.getSkill());
					existingSkill.setSkillLevel(updateSkill.getSkillLevel());
				}
			});
		});
		repository.saveAll(existingSkills);
	}

	@Override
	public Long saveParentEntity(D dto, P parentEntity) {
		List<T> parents = new ArrayList<>();
		Map<String, String> skillMap = convertStringToMap(dto.getSkills());
		skillMap.forEach((skillName, skillLevel) -> {
			Skill newSkill = skillRepo.findByNameIgnoreCase(skillName);
			SkillLevel newLevel = skillLevelRepo.findByNameIgnoreCase(skillLevel);

			T insertSkill = createSkillInstance(null, parentEntity, newSkill, newLevel);
			parents.add(insertSkill);
		});
		repository.saveAll(parents);
		return 1L;
	}

	/**
	 * Map 자료구조를 정규 표현식으로 작성된 문자열로 변환하는 메서드
	 * 
	 * @param skillMap key: 기술, value: 숙련도를 가지는 Map 자료구조
	 * @return 정규 표현식으로 작성된 #기술과 :숙련도로 이루어진 문자열
	 */
	private String convertMapToString(Map<String, String> skillMap) {
		StringBuilder skills = new StringBuilder();

		skillMap.forEach((key, value) -> {
			if (skills.length() > 0) {
				skills.append(", ");
			}
			skills.append("#").append(key).append(":").append(value);
		});

		return skills.toString();
	}

	/**
	 * 정규 표현식으로 작성된 문자열을 Map으로 리턴해주는 변환 메서드
	 * 
	 * @param skills 정규 표현식으로 #기술과 :숙련도로 이루어진 문자열
	 * @return key: 기술, value: 숙련도를 가지는 Map 자료구조 리턴
	 */
	private Map<String, String> convertStringToMap(String skills) {
		String[] skillStrings = skills.split(",");
		Map<String, String> skillMap = new HashMap<>();
		for (String skillString : skillStrings) {
			skillString = skillString.trim(); // 앞뒤 공백 제거
			if (skillString.startsWith("#")) {
				skillString = skillString.substring(1); // # 문자 제거
			}

			String[] skillInfo = skillString.split(":");

			// skillInfo에는 스킬 이름과 기술 숙련도 두 데이터만 들어있어야 함.
			if (skillInfo.length == 2) {
				skillMap.put(skillInfo[0].trim(), skillInfo[1].trim());
			}
		}

		return skillMap;
	}

}

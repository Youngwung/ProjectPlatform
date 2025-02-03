package com.ppp.backend.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ppp.backend.domain.Skill;
import com.ppp.backend.domain.SkillLevel;
import com.ppp.backend.repository.SkillLevelRepository;
import com.ppp.backend.repository.SkillRepository;
import com.ppp.backend.util.SkillDtoConverter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ValidationService {
	private final SkillRepository skillRepo;

	private final SkillLevelRepository skillLevelRepo;

	private final SkillDtoConverter skillDtoConverter;

	/**
	 * 기술 스택 입력 창에서 전달받은 정규 표현식의 유효성을 검사하는 메서드
	 * @param skills 정규 표현식으로 작성된 기술과 숙련도의 문자열
	 * @return 잘못 입력된 문자열을 반환
	 */
	public List<String> validateSkills(String skills) {
		log.info("service skills = ", skills);
		List<String> invalidList = new ArrayList<>();
		Map<String, String> skillMap = skillDtoConverter.convertSkillDtoToMap(skills);

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
		return invalidList;
	}

	public List<String> duplicatedSkills(String skills) {
		Map<String, String> skillMap = skillDtoConverter.convertSkillDtoToMap(skills);

		Set<String> seenKeys = new HashSet<>();
		Set<String> duplicatedKeys = new HashSet<>();
		
		skillMap.forEach((key, value) -> {
			String lowerCaseKey = key.toLowerCase();
			if (seenKeys.contains(lowerCaseKey)) {
				duplicatedKeys.add(key);
			} else {
				seenKeys.add(key);
			}
		});
		
		return new ArrayList<>(duplicatedKeys);
	}
}

package com.ppp.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ppp.backend.domain.Skill;
import com.ppp.backend.domain.SkillCategory;
import com.ppp.backend.dto.skill.SkillDto;
import com.ppp.backend.dto.skill.SkillSearchDto;
import com.ppp.backend.dto.skill.SkillSearchResultDto;
import com.ppp.backend.repository.SkillCategoryRepository;
import com.ppp.backend.repository.SkillRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SkillSearchService {
	private final SkillCategoryRepository skillCategoryRepo;
	private final SkillRepository skillRepo;

	/**
	 * 모든 스킬 카테고리 리스트를 리턴하는 메서드
	 * 
	 * @return List<SkillSearchDto> dtoList
	 */
	public List<SkillSearchDto> getList() {
		log.info("getList()");
		List<SkillCategory> skillCategories = skillCategoryRepo.findAll();
		List<SkillSearchDto> dtoList = skillCategories.stream().map((entity) -> toSearchDto(entity))
				.collect(Collectors.toList());
		return dtoList;
	}

	/**
	 * 검색어를 기반으로 스킬의 이름을 리턴하는 메서드
	 * 
	 * @return List<SkillSearchDto>
	 */
	public List<SkillSearchResultDto> getSearchResult(String keyword) {
		List<Skill> result = skillRepo.searchSkill(keyword);
		List<SkillSearchResultDto> resultDto = result.stream().map((skill) -> toSearchResultDto(skill)).collect(Collectors.toList());
		return resultDto;
	}

	private SkillSearchDto toSearchDto(SkillCategory entity) {
		SkillSearchDto dto = SkillSearchDto.builder()
				.id(entity.getId())
				.name(entity.getName())
				.description(entity.getDescription())
				.build();
		List<Skill> categorySkillList = skillRepo.findBySkillCategoryId(entity.getId());
		List<SkillDto> dtoList = new ArrayList<>();
		categorySkillList.forEach((skill) -> {
			dtoList.add(SkillDto.builder()
					.id(skill.getId())
					.name(skill.getName())
					.skillCategoryId(skill.getSkillCategory().getId())
					.build());
		});
		dto.setSkills(dtoList);
		return dto;
	}

	private SkillSearchResultDto toSearchResultDto(Skill entity) {
		return SkillSearchResultDto.builder()
				.id(entity.getId())
				.skill(entity.getName())
				.skillCategoryName(entity.getSkillCategory().getDescription())
				.build();
	}
}

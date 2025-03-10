package com.ppp.backend.service;

import com.ppp.backend.domain.skill.BaseSkill;
import com.ppp.backend.dto.skill.BaseSkillDto;

public interface SkillService <T extends BaseSkill, D extends BaseSkillDto, P>{
	String getSkill(Long ownerId);
	boolean existingSkill(String skills);
	void modifySkill(Long id, D dto, P parentEntity);
	Long saveParentEntity(D dto, P parentEntity);
}

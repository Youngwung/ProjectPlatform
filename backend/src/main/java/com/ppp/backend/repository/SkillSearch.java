package com.ppp.backend.repository;

import java.util.List;

import com.ppp.backend.domain.Skill;

public interface SkillSearch {
	/**
	 * 키워드로 해당 문자열을 포함하는 스킬을 리턴하는 메서드
	 * @param keyword
	 * @return
	 */
	List<Skill> searchSkill(String keyword);
}

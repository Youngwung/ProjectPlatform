package com.ppp.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.ppp.backend.domain.QSkill;
import com.ppp.backend.domain.Skill;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;

public class SkillSearchImpl extends QuerydslRepositorySupport implements SkillSearch{

	public SkillSearchImpl() {
		super(Skill.class);
	}

	@Override
	public List<Skill> searchSkill(String keyword) {
		QSkill skill = QSkill.skill;

		JPQLQuery<Skill> query = from(skill);

		BooleanExpression skillNameEq = skill.name.contains(keyword);
		query.where(skillNameEq);

		List<Skill> result = query.fetch();
		return result;
	}
	
}

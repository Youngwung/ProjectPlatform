package com.ppp.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ppp.backend.domain.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>, SkillSearch{
	Skill findByNameIgnoreCase(String name);

	List<Skill> findBySkillCategoryId(Long id);
}

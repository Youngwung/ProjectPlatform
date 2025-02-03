package com.ppp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.SkillCategory;

public interface SkillCategoryRepository extends JpaRepository<SkillCategory, Long>{
	
}

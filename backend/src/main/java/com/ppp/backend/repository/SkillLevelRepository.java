package com.ppp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.SkillLevel;

public interface SkillLevelRepository extends JpaRepository<SkillLevel, Long>{
	SkillLevel findByNameIgnoreCase(String name);
}

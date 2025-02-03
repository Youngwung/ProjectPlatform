package com.ppp.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.ProjectSkill;

public interface ProjectSkillRepository extends JpaRepository<ProjectSkill, Long> {
	List<ProjectSkill> findByProjectId(Long id);

	int deleteByProjectId(Long id);
}

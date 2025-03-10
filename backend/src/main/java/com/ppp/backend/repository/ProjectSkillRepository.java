package com.ppp.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ppp.backend.domain.ProjectSkill;

public interface ProjectSkillRepository extends BaseSkillRepository<ProjectSkill> {
	List<ProjectSkill> findByProjectId(Long id);

	int deleteByProjectId(Long id);

	@Override
	@Query("""
				SELECT p FROM ProjectSkill p WHERE p.project.id = :ownerId
			""")
	List<ProjectSkill> findByOwner(@Param("ownerId") Long ownerId);
}

package com.ppp.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.JoinProjectSkill;

public interface JoinProjectSkillRepository extends JpaRepository<JoinProjectSkill, Long> {
	List<JoinProjectSkill> findByJoinProjectId(Long id);

}

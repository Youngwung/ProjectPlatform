package com.ppp.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.ppp.backend.domain.skill.BaseSkill;

@NoRepositoryBean
public interface BaseSkillRepository<T extends BaseSkill> extends JpaRepository<T, Long>{
	List<T> findByOwner(Long ownerId);
}

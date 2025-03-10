package com.ppp.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppp.backend.domain.ProjectType;

public interface ProjectTypeRepository extends JpaRepository<ProjectType, Long> {
	Optional<ProjectType> findByProjectId(Long projectId);
}

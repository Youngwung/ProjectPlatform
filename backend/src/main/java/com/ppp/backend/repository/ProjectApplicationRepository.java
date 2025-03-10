package com.ppp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ppp.backend.domain.ProjectApplication;

@Repository
public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long>{
	
}
